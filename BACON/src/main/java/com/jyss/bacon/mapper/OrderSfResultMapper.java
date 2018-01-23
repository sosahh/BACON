package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.OrderSfResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSfResultMapper {

    /**
     * 条件查询
     * @param orderId
     * @return
     */
    List<OrderSfResult> selectOrderSfResult(@Param("orderId")String orderId);

}