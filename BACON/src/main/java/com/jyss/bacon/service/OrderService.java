package com.jyss.bacon.service;

import com.jyss.bacon.entity.OrderSf;
import com.jyss.bacon.entity.ResponseResult;
import org.apache.ibatis.annotations.Param;


public interface OrderService {

    //添加上分订单
    int insert(OrderSf orderSf);

    //上分订单支付
    ResponseResult updateOrderSf(@Param("uId")Integer uId,@Param("oId") Integer oId);


}
