package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.OrderSf;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSfMapper {

    int insert(OrderSf orderSf);

    int updateByPrimaryKeySelective(OrderSf orderSf);

    int updateByPrimaryKey(OrderSf orderSf);
}