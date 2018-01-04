package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.OrderSfResult;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSfResultMapper {

    int insert(OrderSfResult orderSfResult);

    int updateByPrimaryKeySelective(OrderSfResult orderSfResult);

    int updateByPrimaryKey(OrderSfResult orderSfResult);
}