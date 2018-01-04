package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.OrderPw;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPwMapper {

    int insert(OrderPw orderPw);

    int updateByPrimaryKeySelective(OrderPw orderPw);

    int updateByPrimaryKey(OrderPw orderPw);
}