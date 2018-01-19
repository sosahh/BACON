package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.OrderSf;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSfMapper {

    //添加上分订单
    int insert(OrderSf orderSf);

    int updateByPrimaryKeySelective(OrderSf orderSf);

    //条件查询上分订单
    List<OrderSf> selectOrderSfBy(@Param("orderId")String orderId,@Param("id")Integer id,
                                  @Param("uId")Integer uId,@Param("status")Integer status);



}