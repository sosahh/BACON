package com.jyss.bacon.action;

import com.jyss.bacon.entity.*;
import com.jyss.bacon.service.ItemService;
import com.jyss.bacon.service.MobileLoginService;
import com.jyss.bacon.service.OrderService;
import com.jyss.bacon.utils.Base64Image;
import com.jyss.bacon.utils.CommTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/order")
@Controller
public class OrderAction {

    @Autowired
    private MobileLoginService mobileLoginService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ItemService itemService;



    /**
     * 上分订单
     * @param orderSf
     * @param token
     * @return
     */
    @RequestMapping(value = "/sfOrder",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult insertOrderSf(OrderSf orderSf,@RequestParam("token") String token){
        if(StringUtils.isEmpty(orderSf.getCount()) ||
                orderSf.getAccount()== null || orderSf.getAccount().trim().length()== 0 ||
                orderSf.getWxAccount()== null || orderSf.getWxAccount().trim().length()== 0){
        return ResponseResult.error("-2","信息不全，无法提交订单！");
        }

        Xtcl xtcl = itemService.getClsValue("discount", "1");
        String value = xtcl.getBz_value();
        double discount = 1 - Double.parseDouble(value);
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            orderSf.setuId(uId);
            orderSf.setOrderId((uId+"")+(System.currentTimeMillis()+""));

            if(orderSf.getIsWin() == 0){
                orderSf.setTotal(orderSf.getCount()*orderSf.getPrice()*discount);   //不计胜负
            }else if(orderSf.getIsWin() == 1){
                orderSf.setTotal(orderSf.getCount()*orderSf.getPrice());            //计胜负
            }
            orderSf.setStatus(0);
            orderSf.setCreated(new Date());
            orderSf.setModifyTime(new Date());
            int count = orderService.insert(orderSf);
            if(count == 1){
                Map<String, Object> map = new HashMap<>();
                map.put("oId",orderSf.getId());
                return ResponseResult.ok(map);
            }

            return ResponseResult.error("-1","提交失败！");
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 订单支付     payType: 1=上分，2=陪玩
     */
    @RequestMapping("/sfPayment")
    @ResponseBody
    public ResponseResult sfOrderPayment(@RequestParam("token") String token,@RequestParam("oId")Integer oId,
                                         @RequestParam("payPwd") String payPwd,@RequestParam("payType")Integer payType){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            if(payType == 1){
                ResponseResult result = orderService.updateOrderSf(uId, oId, payPwd);
                return result;
            }else if (payType == 2){
                ResponseResult result = orderService.updateOrderPw(uId, oId, payPwd);
                return result;
            }
            return ResponseResult.error("-3","支付失败！");
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 上分订单取消
     */
    @RequestMapping("/upOrderSf")
    @ResponseBody
    public ResponseResult updateOrderSfStatus(@RequestParam("token") String token,@RequestParam("oId")Integer oId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = orderService.updateOrderSfStatus(uId, oId);
            return result;

        }
        return ResponseResult.error("1","token失效！");
    }



    /**
     * 上分订单个人查询
     */
    @RequestMapping("/getOrderSf")
    @ResponseBody
    public ResponseResult selectOrderSf(@RequestParam("token") String token,
                                        @RequestParam(value = "page", required = true) Integer page,
                                        @RequestParam(value = "pageSize", required = true) Integer pageSize){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = orderService.getOrderSfByUid(uId, page, pageSize);
            return result;
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 陪玩订单
     */
    @RequestMapping("/pwOrder")
    @ResponseBody
    public ResponseResult selectOrderSf(@RequestParam("token") String token,OrderPw orderPw){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            if(uId == orderPw.getPlayId()){
                return ResponseResult.error("-2","自己不能给自己下单！");
            }
            orderPw.setOrderId((uId+"")+(System.currentTimeMillis()+""));
            orderPw.setuId(uId);
            orderPw.setTotal(orderPw.getCount()*orderPw.getPrice());
            orderPw.setStatus(0);
            orderPw.setOrderReason(0);
            orderPw.setType(1);
            orderPw.setCreated(new Date());
            orderPw.setModifyTime(new Date());
            int count = orderService.insertOrderPw(orderPw);
            if(count == 1){
                Map<String, Object> map = new HashMap<>();
                map.put("oId",orderPw.getId());
                return ResponseResult.ok(map);
            }
            return ResponseResult.error("-1","提交失败！");
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 陪玩订单取消       cclType: 1=用户取消，2=陪玩人取消
     */
    @RequestMapping("/upOrderPw")
    @ResponseBody
    public ResponseResult updateOrderPwStatus(@RequestParam("token") String token,@RequestParam("oId")Integer oId,
                                              @RequestParam("cclType")Integer cclType){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = orderService.updateOrderPwStatus(uId, oId, cclType);
            return result;
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 陪玩订单个人查询    orderType: 1=用户查询，2=约客查询
     */
    @RequestMapping("/getOrderPw")
    @ResponseBody
    public ResponseResult selectOrderPwByUid(@RequestParam("token") String token,@RequestParam("orderType") Integer orderType,
                                             @RequestParam(value = "page", required = true) Integer page,
                                             @RequestParam(value = "pageSize", required = true) Integer pageSize){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = orderService.getOrderPwByUid(uId, orderType, page, pageSize);
            return result;
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 陪玩订单接单
     */
    @RequestMapping("/updatePw")
    @ResponseBody
    public ResponseResult updateOrderPwByPlayId(@RequestParam("token") String token,@RequestParam("oId")Integer oId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = orderService.updateOrderPwByPlayId(uId, oId);
            return result;
        }
        return ResponseResult.error("1","token失效！");

    }

    /**
     * 陪玩订单进行中      陪玩人端开始
     */
    @RequestMapping("/updatePwStatus")
    @ResponseBody
    public ResponseResult updateOrderPwSatusByPlayId(@RequestParam("token") String token,@RequestParam("oId")Integer oId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = orderService.updateOrderPwSatusByPlayId(uId, oId);
            return result;
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 陪玩订单确认完成      陪玩人端确认
     */
    @RequestMapping("/confirmOrder")
    @ResponseBody
    public ResponseResult updateOrderPwBy(@RequestParam("token") String token,@RequestParam("oId")Integer oId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = orderService.updateOrderPwBy(uId, oId);
            return result;
        }
        return ResponseResult.error("1","token失效！");
    }



    /**
     * 删除未支付订单     dltType:  1=上分订单删除，2=陪玩订单删除
     */
    @RequestMapping("/deleteOrder")
    @ResponseBody
    public ResponseResult deleteOrderByUid(@RequestParam("token") String token,@RequestParam("oId")Integer oId,
                                           @RequestParam("dltType")Integer dltType){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = orderService.deleteOrderByUid(uId, oId, dltType);
            return result;
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 评价订单
     */
    @RequestMapping("/evaluate")
    @ResponseBody
    public ResponseResult insertOrderEvaluate(OrderEvaluate orderEvaluate, @RequestParam("token") String token,
                                              HttpServletRequest request, HttpServletResponse response) throws Exception{
        if(StringUtils.isEmpty(orderEvaluate.getContent())){
            return ResponseResult.error("-3","内容不能为空！");
        }
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<OrderEvaluate> evaluateList = orderService.selectEvaluateBy(uId, orderEvaluate.getoId());
            if(evaluateList != null && evaluateList.size()>0){
                return ResponseResult.error("-2","已经评价！");
            }
            orderEvaluate.setuId(uId);
            orderEvaluate.setStatus(1);
            orderEvaluate.setCreated(new Date());

            // Base64.decode(photo);
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html");

            String filePath = request.getSession().getServletContext().getRealPath("/");
            int index = filePath.indexOf("BACON");
            filePath = filePath.substring(0, index) + "uploadDyPic" + "/";
            File f = new File(filePath);
            CommTool.judeDirExists(f);
            String filePath1 = filePath + uId + System.currentTimeMillis() + "001.png";
            String filePath2 = filePath + uId + System.currentTimeMillis() + "002.png";
            String filePath3 = filePath + uId + System.currentTimeMillis() + "003.png";
            boolean isOk1 = false;
            isOk1 = Base64Image.GenerateImage(orderEvaluate.getPicture1(), filePath1);
            if (isOk1) {
                orderEvaluate.setPicture1(filePath1.substring(filePath1.indexOf("uploadDyPic")));
            }
            boolean isOk2 = false;
            isOk2 = Base64Image.GenerateImage(orderEvaluate.getPicture2(), filePath2);
            if (isOk2) {
                orderEvaluate.setPicture2(filePath2.substring(filePath2.indexOf("uploadDyPic")));
            }
            boolean isOk3 = false;
            isOk3 = Base64Image.GenerateImage(orderEvaluate.getPicture3(), filePath3);
            if (isOk3) {
                orderEvaluate.setPicture3(filePath3.substring(filePath3.indexOf("uploadDyPic")));
            }
            int count = orderService.insertEvaluate(orderEvaluate);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","评价失败！");
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 上分订单详情
     */
    @RequestMapping("/sfDetails")
    @ResponseBody
    public ResponseResult getOrderSfDetails(@RequestParam("token") String token,@RequestParam("oId")Integer oId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();

            return null;
        }
        return ResponseResult.error("1","token失效！");
    }



    /**
     * 陪玩订单详情    pwType:  1=用户端，2=陪玩人端
     */
    @RequestMapping("/pwDetails")
    @ResponseBody
    public ResponseResult getOrderPwDetails(@RequestParam("token") String token,@RequestParam("oId")Integer oId,
                                            @RequestParam("pwType")Integer pwType){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = orderService.getOrderPwDetails(uId, oId, pwType);
            return result;
        }
        return ResponseResult.error("1","token失效！");
    }



}
