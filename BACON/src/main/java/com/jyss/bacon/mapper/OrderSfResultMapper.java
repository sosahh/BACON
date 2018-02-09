package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.OrderSfResult;
import com.jyss.bacon.entity.OrderSfView;
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

    /**
     * //查询结果订单里的获得总金额==status 1=接单，2=完成订单
     * @param sfUserId
     * @return
     */
    OrderSfResult getOrderSfSumTotal(@Param("sfUserId")String sfUserId);

    /**
     *  上分订单结果获得总金额[订单金额，实得金额]
     *status = 0未支付，1已支付，2已接单，3完成，4订单取消
     *reStatus=1 =分配订单 2=完成订单 3=取消订单
     * @param sfUserId
     * @param status
     * @param reStatus
     * @return
     */
    OrderSfView getSfOrderSumTotal(@Param("sfUserId")String sfUserId,@Param("status")String status,@Param("reStatus")String reStatus);

    /**
     *  上分订单结果详情查询
     *status = 0未支付，1已支付，2已接单，3完成，4订单取消
     *reStatus=1 =分配订单 2=完成订单 3=取消订单
     * @param sfUserId
     * @param status
     * @param reStatus
     * @return
     */
    List<OrderSfView> getSfOrderResultInfo(@Param("sfUserId")String sfUserId,@Param("status")String status,@Param("reStatus")String reStatus);


}