package com.jyss.bacon.service;

import com.jyss.bacon.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;


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

    //陪玩订单进行中
    ResponseResult updateOrderPwSatusByPlayId(@Param("uId")Integer uId,@Param("oId") Integer oId);

    //陪玩订单确认完成
    ResponseResult updateOrderPwBy(@Param("uId")Integer uId,@Param("oId") Integer oId);

    //删除未支付订单
    ResponseResult deleteOrderByUid(@Param("uId")Integer uId,@Param("oId") Integer oId,@Param("dltType") Integer dltType);

    //添加订单评价
    int insertEvaluate(OrderEvaluate orderEvaluate);

    //条件查询订单评价
    List<OrderEvaluate> selectEvaluateBy(@Param("uId")Integer uId, @Param("oId")Integer oId);

    //上分订单详情
    ResponseResult getOrderSfDetails(@Param("uId")Integer uId, @Param("oId")Integer oId);

    //陪玩订单详情
    ResponseResult getOrderPwDetails(@Param("uId")Integer uId, @Param("oId")Integer oId , @Param("pwType")Integer pwType);

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

    /**
     * 修改订单表状态
     * @param orderId
     * @param status
     * @param statusBefore
     * @return
     */
    int upOrderSf(@Param("orderId")String orderId,@Param("status")String status,@Param("statusBefore")String statusBefore);

    /**
     * 修改订单结果表数据
     * @param orderId
     * @param sfStar
     * @param result
     * @param pictures
     * @param status
     * @param statusBefore
     * @return
     */
    int upOrderSfResult(@Param("orderId")String orderId,@Param("sfStar")String sfStar,@Param("result")String result,@Param("pictures")String pictures,
                        @Param("status")String status,@Param("statusBefore")String statusBefore);

    ////自定义修改订单结果方法
    int upMyOrderResult(OrderSfResult os);

    /**
     * 添加提现记录
     * @param dlAppEarn
     * @return
     */
    int insertDlScoreEarn(DlAppEarn dlAppEarn);

    int addDlScoreEarn(DlAppEarn dlAppEarn,double balance);

}
