package com.jyss.bacon.action;

import com.jyss.bacon.entity.*;
import com.jyss.bacon.service.ItemService;
import com.jyss.bacon.service.MobileLoginService;
import com.jyss.bacon.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * 上分订单支付
     */
    @RequestMapping("/sfPayment")
    @ResponseBody
    public ResponseResult sfOrderPayment(@RequestParam("token") String token,@RequestParam("oId")Integer oId,
                                         @RequestParam("payPwd") String payPwd){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = orderService.updateOrderSf(uId, oId, payPwd);
            return result;

        }
        return ResponseResult.error("1","token失效！");
    }





    /**
     * 上分订单取消
     */



}
