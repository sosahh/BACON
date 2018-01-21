package com.jyss.bacon.service;

import com.jyss.bacon.entity.OrderPw;
import com.jyss.bacon.entity.OrderSf;
import com.jyss.bacon.entity.ResponseResult;
import org.apache.ibatis.annotations.Param;


public interface OrderService {

    //添加上分订单
    int insert(OrderSf orderSf);

    //上分订单支付
    ResponseResult updateOrderSf(@Param("uId")Integer uId,@Param("oId") Integer oId,@Param("payPwd") String payPwd);

    //上分订单取消
    ResponseResult updateOrderSfStatus(@Param("uId")Integer uId,@Param("oId") Integer oId);

    //上分订单个人查询
    ResponseResult getOrderSfByUid(@Param("uId")Integer uId,@Param("page")Integer page,@Param("pageSize")Integer pageSize);

    //添加陪玩订单
    int insertOrderPw(OrderPw orderPw);

    //陪玩订单支付
    ResponseResult updateOrderPw(@Param("uId")Integer uId,@Param("oId") Integer oId,@Param("payPwd") String payPwd);

    //陪玩订单取消
    ResponseResult updateOrderPwStatus(@Param("uId")Integer uId,@Param("oId") Integer oId,@Param("cclType") Integer cclType);

    //陪玩订单查询
    ResponseResult getOrderPwByUid(@Param("uId")Integer uId,@Param("orderType")Integer orderType,
                                   @Param("page")Integer page,@Param("pageSize")Integer pageSize);

    //陪玩订单接单
    ResponseResult updateOrderPwByPlayId(@Param("uId")Integer uId,@Param("oId") Integer oId);

    //陪玩订单确认完成
    ResponseResult updateOrderPwBy(@Param("uId")Integer uId,@Param("oId") Integer oId);

    //删除未支付订单
    ResponseResult deleteOrderByUid(@Param("uId")Integer uId,@Param("oId") Integer oId,@Param("dltType") Integer dltType);

}
