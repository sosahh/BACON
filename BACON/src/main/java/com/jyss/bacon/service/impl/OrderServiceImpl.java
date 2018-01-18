package com.jyss.bacon.service.impl;

import com.jyss.bacon.entity.OrderSf;
import com.jyss.bacon.mapper.OrderSfMapper;
import com.jyss.bacon.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderSfMapper orderSfMapper;

    @Override
    public int insert(OrderSf orderSf) {
        return orderSfMapper.insert(orderSf);
    }


}
