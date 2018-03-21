package com.jyss.bacon.action;

import com.github.pagehelper.PageInfo;
import com.jyss.bacon.entity.*;
import com.jyss.bacon.service.ItemService;
import com.jyss.bacon.service.MobileLoginService;
import com.jyss.bacon.service.OrderService;
import com.jyss.bacon.service.UserService;
import com.jyss.bacon.utils.Base64Image;
import com.jyss.bacon.utils.CommTool;
import com.jyss.bacon.utils.PasswordUtil;
import com.jyss.bacon.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private UserService userService;



    /**
     * 上分订单                IsWin:  0不计胜负，1计胜负
     * @param orderSf
     * @param token
     * @return
     */
    @RequestMapping(value = "/sfOrder",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult insertOrderSf(OrderSf orderSf,@RequestParam("mjTime")Double mjTime,@RequestParam("token") String token){
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
                orderSf.setTotal(orderSf.getCount()*orderSf.getPrice()*discount);   //不计胜负,享受折扣
            }else if(orderSf.getIsWin() == 1){
                orderSf.setTotal(orderSf.getCount()*orderSf.getPrice());            //计胜负
            }
            orderSf.setStatus(0);
            orderSf.setCreated(new Date());
            orderSf.setModifyTime(new Date());
            orderSf.setAcceptTime(orderSf.getCount()* mjTime);
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
     * 陪玩订单确认完成      qrType：1=陪玩人确认，2=下单人确认
     */
    @RequestMapping("/confirmOrder")
    @ResponseBody
    public ResponseResult updateOrderPwBy(@RequestParam("token") String token,
                                          @RequestParam("oId")Integer oId,@RequestParam("qrType")Integer qrType){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = orderService.updateOrderPwBy(uId, oId, qrType);
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
     * 评价订单
     */
    @RequestMapping("/evaluate1")
    @ResponseBody
    public ResponseResult insertOrderEvaluate(OrderEvaluate orderEvaluate, @RequestParam("token") String token){
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

            if(!StringUtils.isEmpty(orderEvaluate.getPicture1())){
                orderEvaluate.setPicture1(orderEvaluate.getPicture1());
            }
            if(!StringUtils.isEmpty(orderEvaluate.getPicture2())){
                orderEvaluate.setPicture1(orderEvaluate.getPicture2());
            }
            if(!StringUtils.isEmpty(orderEvaluate.getPicture3())){
                orderEvaluate.setPicture1(orderEvaluate.getPicture3());
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
            ResponseResult result = orderService.getOrderSfDetails(uId, oId);
            return result;
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



    /////////////////代练端APP///////////////////////
//     *  上分订单结果获得总金额[订单金额，实得金额]
//     *status = 0未支付，1已支付，2已接单，3完成，4订单取消
//     *reStatus=1 =分配订单 2=完成订单 3=取消订单
    /////未完成订单
    @RequestMapping("/sf/getSfOrderJdInfo")
    @ResponseBody
    public ResponseResult getSfOrderJdInfo(@RequestParam("token") String token,@RequestParam(value = "page", required = true) int page,
                                           @RequestParam(value = "limit", required = true) int limit){
        List<MobileLogin> loginList = mobileLoginService.findUserByTokenBySf(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<OrderSfView> infoList = orderService.getSfOrderResultInfo(uId.toString(),"2","1");
            PageInfo<OrderSfView> pageInfoOrder = new PageInfo<OrderSfView>(infoList);
            return ResponseResult.ok(new Page<OrderSfView>(pageInfoOrder));
        }
        return ResponseResult.error("1","token失效！");
    }

    /////已经完成订单
    @RequestMapping("/sf/getSfOrderFinishInfo")
    @ResponseBody
    public ResponseResult getSfOrderFinishInfo(@RequestParam("token") String token,@RequestParam(value = "page", required = true) int page,
                                           @RequestParam(value = "limit", required = true) int limit){
        List<MobileLogin> loginList = mobileLoginService.findUserByTokenBySf(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<OrderSfView> infoList1 = orderService.getSfOrderResultInfo(uId.toString(),"3","2");
            PageInfo<OrderSfView> pageInfoOrder = new PageInfo<OrderSfView>(infoList1);
            return ResponseResult.ok(new Page<OrderSfView>(pageInfoOrder));
        }
        return ResponseResult.error("1","token失效！");
    }

    /////账单明细
    @RequestMapping("/sf/getDrawCashDetails")
    @ResponseBody
    public ResponseResult getDrawCashDetails(@RequestParam("token") String token,@RequestParam(value = "page", required = true) int page,
                                               @RequestParam(value = "limit", required = true) int limit){
        List<MobileLogin> loginList = mobileLoginService.findUserByTokenBySf(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<DrawCashDetails> infoList1 = orderService.getDrawCashDetails(uId.toString());
            PageInfo<DrawCashDetails> pageInfoOrder = new PageInfo<DrawCashDetails>(infoList1);
            return ResponseResult.ok(new Page<DrawCashDetails>(pageInfoOrder));
        }
        return ResponseResult.error("1","token失效！");
    }



    //////完成订单，上传结果///////
    /**
     * /完成订单，上传结果（文件上传)
     */
    @RequestMapping("/sf/finishOrder")
    @ResponseBody
    public ResponseResult finishOrder(OrderSfResult osResult, @RequestParam("token")String token,
                                            MultipartFile pic1, MultipartFile pic2, MultipartFile pic3,
                                            MultipartFile pic4, MultipartFile pic5, MultipartFile pic6,
                                            HttpServletRequest request) throws Exception {
        List<MobileLogin> loginList = mobileLoginService.findUserByTokenBySf(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<UserSf> userList = userService.selectUserSfBy(uId.toString(),null,"1");
            if (userList==null||userList.size()!=1){
                return ResponseResult.error("-3","用户信息发生错误！");
            }
            UserSf  sf = userList.get(0);
            double myTotalBalance = sf.getBalance();
            /////查询订单金额///
            List<OrderSfResult> llist = orderService.getResultInfo(uId.toString(),osResult.getOrderId(),"1");
            if (llist==null||llist.size()!=1){
                return ResponseResult.error("-4","订单信息异常！");
            }
            double  orderBalance = llist.get(0).getFinishMoney();
            myTotalBalance = myTotalBalance + orderBalance;
            //图片上传
            String pictures ="";
            String filePath = request.getSession().getServletContext().getRealPath("/");
            int index = filePath.indexOf("BACON");
            filePath = filePath.substring(0, index) + "uploadResultImg" + "/";
            if(!StringUtils.isEmpty(pic1)){
                String filename1 = pic1.getOriginalFilename();
                filename1 = new String(filename1.getBytes("iso8859-1"), "utf-8");
                String extName1 = filename1.substring(filename1.lastIndexOf("."));
                String imgPath1 = filePath + uId + System.currentTimeMillis() + "01" + extName1;
                if (!Utils.saveUpload(pic1, imgPath1)) {
                   return ResponseResult.error("-2","文件上传失败");
                }
                imgPath1 = imgPath1.substring(imgPath1.indexOf("uploadResultImg"));
                pictures = pictures+imgPath1+";";
            }
            if(!StringUtils.isEmpty(pic2)){
                String filename2 = pic2.getOriginalFilename();
                filename2 = new String(filename2.getBytes("iso8859-1"), "utf-8");
                String extName2 = filename2.substring(filename2.lastIndexOf("."));
                String imgPath2 = filePath + uId + System.currentTimeMillis() + "02" + extName2;
                if (!Utils.saveUpload(pic2, imgPath2)) {
                    return ResponseResult.error("-2","文件上传失败");
                }
                imgPath2 = imgPath2.substring(imgPath2.indexOf("uploadResultImg"));
                pictures = pictures+imgPath2+";";
            }
            if(!StringUtils.isEmpty(pic3)){
                String filename3 = pic3.getOriginalFilename();
                filename3 = new String(filename3.getBytes("iso8859-1"), "utf-8");
                String extName3 = filename3.substring(filename3.lastIndexOf("."));
                String imgPath3 = filePath + uId + System.currentTimeMillis() + "03" + extName3;
                if (!Utils.saveUpload(pic3, imgPath3)) {
                    return ResponseResult.error("-2","文件上传失败");
                }
                imgPath3= imgPath3.substring(imgPath3.indexOf("uploadResultImg"));
                pictures = pictures+imgPath3+";";
            }
            if(!StringUtils.isEmpty(pic4)){
                String filename4 = pic4.getOriginalFilename();
                filename4 = new String(filename4.getBytes("iso8859-1"), "utf-8");
                String extName4 = filename4.substring(filename4.lastIndexOf("."));
                String imgPath4 = filePath + uId + System.currentTimeMillis() + "04" + extName4;
                if (!Utils.saveUpload(pic4, imgPath4)) {
                    return ResponseResult.error("-2","文件上传失败");
                }
                imgPath4 = imgPath4.substring(imgPath4.indexOf("uploadResultImg"));
                pictures = pictures+imgPath4+";";
            }
            if(!StringUtils.isEmpty(pic5)){
                String filename5 = pic5.getOriginalFilename();
                filename5 = new String(filename5.getBytes("iso8859-1"), "utf-8");
                String extName5 = filename5.substring(filename5.lastIndexOf("."));
                String imgPath5 = filePath + uId + System.currentTimeMillis() + "05" + extName5;
                if (!Utils.saveUpload(pic5, imgPath5)) {
                    return ResponseResult.error("-2","文件上传失败");
                }
                imgPath5 = imgPath5.substring(imgPath5.indexOf("uploadResultImg"));
                pictures = pictures+imgPath5+";";
            }
            if(!StringUtils.isEmpty(pic6)){
                String filename6 = pic6.getOriginalFilename();
                filename6 = new String(filename6.getBytes("iso8859-1"), "utf-8");
                String extName6 = filename6.substring(filename6.lastIndexOf("."));
                String imgPath6 = filePath + uId + System.currentTimeMillis() + "06" + extName6;
                if (!Utils.saveUpload(pic6, imgPath6)) {
                    return ResponseResult.error("-2","文件上传失败");
                }
                imgPath6= imgPath6.substring(imgPath6.indexOf("uploadResultImg"));
                pictures = pictures+imgPath6+";";
            }
            osResult.setPicture(pictures);
            osResult.setSfUserId(uId);
            int count = orderService.updateMyOrderResult(osResult,myTotalBalance);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","上传结果失败！");
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * /完成订单，上传结果（文件上传)
     */
    @RequestMapping("/sf/finishOrder1")
    @ResponseBody
    public ResponseResult finishOrder(OrderSfResult osResult, @RequestParam("token")String token,
                                      @RequestParam("pic1")String pic1,@RequestParam("pic2")String pic2,
                                      @RequestParam("pic3")String pic3,@RequestParam("pic4")String pic4,
                                      @RequestParam("pic5")String pic5,@RequestParam("pic6")String pic6){
        List<MobileLogin> loginList = mobileLoginService.findUserByTokenBySf(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<UserSf> userList = userService.selectUserSfBy(uId.toString(),null,"1");
            if (userList==null||userList.size()!=1){
                return ResponseResult.error("-3","用户信息发生错误！");
            }
            UserSf  sf = userList.get(0);
            double myTotalBalance = sf.getBalance();
            /////查询订单金额///
            List<OrderSfResult> llist = orderService.getResultInfo(uId.toString(),osResult.getOrderId(),"1");
            if (llist==null||llist.size()!=1){
                return ResponseResult.error("-4","订单信息异常！");
            }
            double  orderBalance = llist.get(0).getFinishMoney();
            myTotalBalance = myTotalBalance + orderBalance;
            //图片上传
            StringBuilder pictures = new StringBuilder();
            if(!StringUtils.isEmpty(pic1)){
                pictures.append(pic1).append(";");
            }
            if(!StringUtils.isEmpty(pic2)){
                pictures.append(pic2).append(";");
            }
            if(!StringUtils.isEmpty(pic3)){
                pictures.append(pic3).append(";");
            }
            if(!StringUtils.isEmpty(pic4)){
                pictures.append(pic4).append(";");
            }
            if(!StringUtils.isEmpty(pic5)){
                pictures.append(pic5).append(";");
            }
            if(!StringUtils.isEmpty(pic6)){
                pictures.append(pic6);
            }

            osResult.setPicture(pictures.toString());
            osResult.setSfUserId(uId);
            int count = orderService.updateMyOrderResult(osResult,myTotalBalance);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","上传结果失败！");
        }
        return ResponseResult.error("1","token失效！");

    }



    /**
     * 提现申请
     */
    @RequestMapping("/sf/drawCash")
    @ResponseBody
    public ResponseResult drawCash(@RequestParam("token")String token,@RequestParam("zfPwd")String zfPwd,@RequestParam("cash")double cash) throws Exception {
        List<MobileLogin> loginList = mobileLoginService.findUserByTokenBySf(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<UserSf> userList = userService.selectUserSfBy(uId.toString(),null,"1");
            UserSf userSf =null;
            if(userList != null && userList.size()==1){
                userSf = userList.get(0);
                if (userSf==null){
                    return ResponseResult.error("-2","用户信息错误！");
                }
                if (!(PasswordUtil.generatePayPwd(zfPwd).equals(userSf.getZfPassword()))){
                    return ResponseResult.error("-5","支付密码错误！");
                }
            }else{
                return ResponseResult.error("-2","用户信息错误！");
            }
            if (userSf.getBalance()<cash){
                return ResponseResult.error("-3","余额不足！");
            }
            if (100>cash){
                return ResponseResult.error("-6","每次提现不得小于100");
            }
            double leftBalance = userSf.getBalance()-cash;
            if (userSf.getZfAccount()==null||userSf.getZfAccount().equals("")||userSf.getZfName()==null||userSf.getZfName().equals("")){
                return ResponseResult.error("-4","无对应提现账户！");
            }
            DlAppEarn dlAppEarn = new  DlAppEarn();
            dlAppEarn.setuId(uId);
            dlAppEarn.setScore(cash);
            dlAppEarn.setRealName(userSf.getZfAccount());
            dlAppEarn.setZfAccount(userSf.getZfName());
            /////提交申请，对应账户余额减少
            int count = orderService.addDlScoreEarn(dlAppEarn,leftBalance);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","提交申请失败！");
        }
        return ResponseResult.error("1","token失效！");

    }


}
