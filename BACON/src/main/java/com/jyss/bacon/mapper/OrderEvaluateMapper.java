package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.OrderEvaluate;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEvaluateMapper {

    int insert(OrderEvaluate orderEvaluate);

    int updateByPrimaryKeySelective(OrderEvaluate orderEvaluate);

    int updateByPrimaryKey(OrderEvaluate orderEvaluate);
}