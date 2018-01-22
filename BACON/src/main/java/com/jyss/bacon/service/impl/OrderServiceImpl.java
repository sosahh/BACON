package com.jyss.bacon.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.bacon.entity.*;
import com.jyss.bacon.mapper.*;
import com.jyss.bacon.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderSfMapper orderSfMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ScoreBalanceMapper scoreBalanceMapper;
    @Autowired
    private OrderPwMapper orderPwMapper;
    @Autowired
    private OrderEvaluateMapper orderEvaluateMapper;



    @Override
    public int insert(OrderSf orderSf) {
        return orderSfMapper.insert(orderSf);
    }


    /**
     * 上分订单支付
     * @param uId
     * @param oId
     * @return
     */
    @Override
    public ResponseResult updateOrderSf(Integer uId, Integer oId, String payPwd) {
        List<User> userList = userMapper.selectUserBy(uId + "", null, null);
        User user = userList.get(0);
        if(DigestUtils.md5DigestAsHex(payPwd.getBytes()).equals(user.getPayPwd())){
            List<OrderSf> orderSfList = orderSfMapper.selectOrderSfBy(null, oId, uId, 0);
            if(orderSfList != null && orderSfList.size()>0){
                OrderSf orderSf = orderSfList.get(0);
                double jyScore = user.getBalance() - orderSf.getTotal();
                if(jyScore >= 0){
                    User user1 = new User();
                    user1.setuId(uId);
                    user1.setBalance((float) jyScore);
                    user1.setLastModifyTime(new Date());
                    int count = userMapper.updateByPrimaryKeySelective(user1);
                    if(count == 1){
                        OrderSf orderSf1 = new OrderSf();
                        orderSf1.setId(oId);
                        orderSf1.setStatus(1);
                        orderSf1.setModifyTime(new Date());
                        int count1 = orderSfMapper.updateByPrimaryKeySelective(orderSf1);
                        if(count1 == 1){
                            ScoreBalance scoreBalance = new ScoreBalance();
                            scoreBalance.setEnd(1);
                            scoreBalance.setuId(uId);
                            scoreBalance.setType(2);
                            scoreBalance.setScore(orderSf.getTotal());
                            scoreBalance.setJyScore(jyScore);
                            scoreBalance.setOrderSn(orderSf.getOrderId());
                            scoreBalance.setStatus(1);
                            scoreBalance.setCreatedAt(new Date());
                            int count2 = scoreBalanceMapper.insert(scoreBalance);
                            if(count2 == 1){
                                return ResponseResult.ok("");
                            }
                        }
                    }
                    return ResponseResult.error("-3","支付失败！");
                }
                return ResponseResult.error("-2","余额不足！");
            }
            return ResponseResult.error("-1","订单异常！");
        }
        return ResponseResult.error("-4","支付密码不正确！");
    }


    /**
     * 上分订单取消
     * @param uId
     * @param oId
     * @return
     */
    @Override
    public ResponseResult updateOrderSfStatus(Integer uId, Integer oId) {
        List<User> userList = userMapper.selectUserBy(uId + "", null, null);
        User user = userList.get(0);
        List<OrderSf> orderSfList = orderSfMapper.selectOrderSfBy(null, oId, uId, 1);
        if(orderSfList != null && orderSfList.size()>0){
            OrderSf orderSf = orderSfList.get(0);
            double jyScore = user.getBalance() + orderSf.getTotal();
            User user1 = new User();
            user1.setuId(uId);
            user1.setBalance((float) jyScore);
            user1.setLastModifyTime(new Date());
            int count = userMapper.updateByPrimaryKeySelective(user1);
            if(count == 1){
                OrderSf orderSf1 = new OrderSf();
                orderSf1.setId(oId);
                orderSf1.setStatus(4);
                orderSf1.setModifyTime(new Date());
                int count1 = orderSfMapper.updateByPrimaryKeySelective(orderSf1);
                if(count1 == 1){
                    ScoreBalance scoreBalance = new ScoreBalance();
                    scoreBalance.setEnd(1);
                    scoreBalance.setuId(uId);
                    scoreBalance.setType(1);
                    scoreBalance.setScore(orderSf.getTotal());
                    scoreBalance.setJyScore(jyScore);
                    scoreBalance.setOrderSn(orderSf.getOrderId());
                    scoreBalance.setStatus(1);
                    scoreBalance.setCreatedAt(new Date());
                    int count2 = scoreBalanceMapper.insert(scoreBalance);
                    if(count2 == 1){
                        return ResponseResult.ok("");
                    }
                }
            }
            return ResponseResult.error("-2","取消失败！");

        }
        return ResponseResult.error("-1","订单无法取消！");
    }


    /**
     * 上分订单个人查询
     * @param uId
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult getOrderSfByUid(Integer uId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<OrderSf> orderSfList = orderSfMapper.getOrderSfByUid(uId);
        PageInfo<OrderSf> pageInfo = new PageInfo<>(orderSfList);
        Page<OrderSf> result = new Page<>(pageInfo);

        return ResponseResult.ok(result);
    }


    /**
     * 添加陪玩订单
     * @param orderPw
     * @return
     */
    @Override
    public int insertOrderPw(OrderPw orderPw) {
        return orderPwMapper.insert(orderPw);
    }


    /**
     * 陪玩订单支付
     * @param uId
     * @param oId
     * @param payPwd
     * @return
     */
    @Override
    public ResponseResult updateOrderPw(Integer uId, Integer oId, String payPwd) {
        List<User> userList = userMapper.selectUserBy(uId + "", null, null);
        User user = userList.get(0);
        if(DigestUtils.md5DigestAsHex(payPwd.getBytes()).equals(user.getPayPwd())){

            List<OrderPw> orderPwList = orderPwMapper.selectOrderPwBy(null, oId, uId, 0);
            if(orderPwList != null && orderPwList.size()>0){
                OrderPw orderPw = orderPwList.get(0);
                double jyScore = user.getBalance() - orderPw.getTotal();
                if(jyScore >= 0){
                    User user1 = new User();
                    user1.setuId(uId);
                    user1.setBalance((float) jyScore);
                    user1.setLastModifyTime(new Date());
                    int count = userMapper.updateByPrimaryKeySelective(user1);
                    if(count == 1){
                        OrderPw orderPw1 = new OrderPw();
                        orderPw1.setId(oId);
                        orderPw1.setStatus(1);
                        orderPw1.setModifyTime(new Date());
                        int count1 = orderPwMapper.updateByPrimaryKeySelective(orderPw1);
                        if(count1 == 1){
                            ScoreBalance scoreBalance = new ScoreBalance();
                            scoreBalance.setEnd(2);
                            scoreBalance.setuId(uId);
                            scoreBalance.setType(2);
                            scoreBalance.setScore(orderPw.getTotal());
                            scoreBalance.setJyScore(jyScore);
                            scoreBalance.setOrderSn(orderPw.getOrderId());
                            scoreBalance.setStatus(1);
                            scoreBalance.setCreatedAt(new Date());
                            int count2 = scoreBalanceMapper.insert(scoreBalance);
                            if(count2 == 1){
                                return ResponseResult.ok("");
                            }
                        }

                    }
                    return ResponseResult.error("-3","支付失败！");
                }
                return ResponseResult.error("-2","余额不足！");
            }
            return ResponseResult.error("-1","订单异常！");
        }
        return ResponseResult.error("-4","支付密码不正确！");
    }


    /**
     * 陪玩订单取消   cclType： 1=用户取消，2=陪玩人取消
     * @param uId
     * @param oId
     * @param cclType
     * @return
     */
    @Override
    public ResponseResult updateOrderPwStatus(Integer uId, Integer oId, Integer cclType) {
        if(cclType == 1){
            List<OrderPw> orderPwList = orderPwMapper.selectOrderPwBy(null, oId, uId, 1);
            if(orderPwList != null && orderPwList.size()>0){
                OrderPw orderPw = orderPwList.get(0);
                List<User> userList = userMapper.selectUserBy(uId + "", null, null);
                User user = userList.get(0);
                double jyScore = user.getBalance() + orderPw.getTotal();
                User user1 = new User();
                user1.setuId(uId);
                user1.setBalance((float) jyScore);
                user1.setLastModifyTime(new Date());
                int count = userMapper.updateByPrimaryKeySelective(user1);
                if(count == 1){
                    OrderPw orderPw1 = new OrderPw();
                    orderPw1.setId(oId);
                    orderPw1.setStatus(5);
                    orderPw1.setOrderReason(1);
                    orderPw1.setModifyTime(new Date());
                    int count1 = orderPwMapper.updateByPrimaryKeySelective(orderPw1);
                    if(count1 == 1){
                        ScoreBalance scoreBalance = new ScoreBalance();
                        scoreBalance.setEnd(2);
                        scoreBalance.setuId(uId);
                        scoreBalance.setType(1);
                        scoreBalance.setScore(orderPw.getTotal());
                        scoreBalance.setJyScore(jyScore);
                        scoreBalance.setOrderSn(orderPw.getOrderId());
                        scoreBalance.setStatus(1);
                        scoreBalance.setCreatedAt(new Date());
                        int count2 = scoreBalanceMapper.insert(scoreBalance);
                        if(count2 == 1){
                            return ResponseResult.ok("");
                        }
                    }
                }
                return ResponseResult.error("-2","取消失败！");
            }
            return ResponseResult.error("-1","订单无法取消！");
        }else if(cclType == 2){
            List<OrderPw> orderPwList = orderPwMapper.selectOrderPwBy(uId, oId, null, 1);
            if(orderPwList != null && orderPwList.size()>0){
                OrderPw orderPw = orderPwList.get(0);
                List<User> userList = userMapper.selectUserBy(orderPw.getuId() + "", null, null);
                User user = userList.get(0);
                double jyScore = user.getBalance() + orderPw.getTotal();
                User user1 = new User();
                user1.setuId(orderPw.getuId());
                user1.setBalance((float) jyScore);
                user1.setLastModifyTime(new Date());
                int count = userMapper.updateByPrimaryKeySelective(user1);
                if(count == 1){
                    OrderPw orderPw1 = new OrderPw();
                    orderPw1.setId(oId);
                    orderPw1.setStatus(5);
                    orderPw1.setOrderReason(2);
                    orderPw1.setModifyTime(new Date());
                    int count1 = orderPwMapper.updateByPrimaryKeySelective(orderPw1);
                    if(count1 == 1){
                        ScoreBalance scoreBalance = new ScoreBalance();
                        scoreBalance.setEnd(2);
                        scoreBalance.setuId(orderPw.getuId());
                        scoreBalance.setDetail("陪玩方取消订单");
                        scoreBalance.setType(1);
                        scoreBalance.setScore(orderPw.getTotal());
                        scoreBalance.setJyScore(jyScore);
                        scoreBalance.setOrderSn(orderPw.getOrderId());
                        scoreBalance.setStatus(1);
                        scoreBalance.setCreatedAt(new Date());
                        int count2 = scoreBalanceMapper.insert(scoreBalance);
                        if(count2 == 1){
                            return ResponseResult.ok("");
                        }
                    }
                }
                return ResponseResult.error("-2","取消失败！");
            }
            return ResponseResult.error("-1","订单无法取消！");
        }
        return ResponseResult.error("-1","订单无法取消！");
    }


    /**
     *  陪玩订单查询      orderType: 1=用户查询，2=约客查询
     * @param uId
     * @param orderType
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult getOrderPwByUid(Integer uId, Integer orderType, Integer page, Integer pageSize) {
        if(orderType == 1){
            PageHelper.startPage(page,pageSize);
            List<OrderPw> orderPwList = orderPwMapper.selectOrderPwByUid(uId);
            for (OrderPw orderPw : orderPwList) {
                if(orderPw.getStatus() == 4){
                    List<OrderEvaluate> evaluateList = orderEvaluateMapper.selectEvaluateBy(orderPw.getuId(), orderPw.getId());
                    if(evaluateList != null && evaluateList.size()>0){
                        orderPw.setIsPj(1);         //已评
                    }else {
                        orderPw.setIsPj(0);         //未评
                    }
                }else{
                    orderPw.setIsPj(0);
                }
            }
            PageInfo<OrderPw> pageInfo = new PageInfo<>(orderPwList);
            Page<OrderPw> result = new Page<>(pageInfo);
            return ResponseResult.ok(result);
        }else if(orderType == 2){
            PageHelper.startPage(page,pageSize);
            List<OrderPw> orderPwList = orderPwMapper.selectOrderPwByPlayId(uId);
            PageInfo<OrderPw> pageInfo = new PageInfo<>(orderPwList);
            Page<OrderPw> result = new Page<>(pageInfo);
            return ResponseResult.ok(result);
        }
        return ResponseResult.error("-1","查询失败！");
    }


    /**
     * 陪玩订单接单
     * @param uId
     * @param oId
     * @return
     */
    @Override
    public ResponseResult updateOrderPwByPlayId(Integer uId, Integer oId) {
        List<OrderPw> orderPwList = orderPwMapper.selectOrderPwBy(uId, oId, null, 1);
        if(orderPwList != null && orderPwList.size()>0){
            OrderPw orderPw = new OrderPw();
            orderPw.setId(oId);
            orderPw.setStatus(2);
            orderPw.setSingleTime(new Date());
            orderPw.setModifyTime(new Date());
            int count = orderPwMapper.updateByPrimaryKeySelective(orderPw);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-2","接单失败！");
        }
        return ResponseResult.error("-1","订单异常！");
    }

    /**
     * 陪玩订单进行中    陪玩人端开始
     * @param uId
     * @param oId
     * @return
     */
    @Override
    public ResponseResult updateOrderPwSatusByPlayId(Integer uId, Integer oId) {
        List<OrderPw> orderPwList = orderPwMapper.selectOrderPwBy(uId, oId, null, 2);
        if(orderPwList != null && orderPwList.size()>0){
            OrderPw orderPw1 = new OrderPw();
            orderPw1.setId(oId);
            orderPw1.setStatus(3);
            orderPw1.setModifyTime(new Date());
            int count = orderPwMapper.updateByPrimaryKeySelective(orderPw1);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-2","操作失败！");
        }
        return ResponseResult.error("-1","订单异常！");
    }


    /**
     * 陪玩订单确认完成     陪玩人端确认
     * @param uId
     * @param oId
     * @return
     */
    @Override
    public ResponseResult updateOrderPwBy(Integer uId, Integer oId) {
        List<OrderPw> orderPwList = orderPwMapper.selectOrderPwBy(uId, oId, null, 3);
        if(orderPwList != null && orderPwList.size()>0){
            OrderPw orderPw = orderPwList.get(0);
            List<User> userList = userMapper.selectUserBy(uId + "", null, null);
            User user = userList.get(0);
            double jyScore = user.getBalance() + orderPw.getTotal();
            User user1 = new User();
            user1.setuId(uId);
            user1.setBalance((float) jyScore);
            user1.setLastModifyTime(new Date());
            int count = userMapper.updateByPrimaryKeySelective(user1);
            if(count == 1){
                OrderPw orderPw1 = new OrderPw();
                orderPw1.setId(oId);
                orderPw1.setStatus(4);
                orderPw1.setModifyTime(new Date());
                int count1 = orderPwMapper.updateByPrimaryKeySelective(orderPw1);
                if(count1 == 1){
                    ScoreBalance scoreBalance = new ScoreBalance();
                    scoreBalance.setEnd(2);
                    scoreBalance.setuId(uId);
                    scoreBalance.setType(1);
                    scoreBalance.setScore(orderPw.getTotal());
                    scoreBalance.setJyScore(jyScore);
                    scoreBalance.setOrderSn(orderPw.getOrderId());
                    scoreBalance.setStatus(1);
                    scoreBalance.setCreatedAt(new Date());
                    int count2 = scoreBalanceMapper.insert(scoreBalance);
                    if(count2 == 1){
                        return ResponseResult.ok("");
                    }
                }
            }
            return ResponseResult.error("-2","确认完成失败！");
        }
        return ResponseResult.error("-1","订单异常！");
    }

    /**
     * 删除未支付订单     dltType:  1=上分订单删除，2=陪玩订单删除
     * @param uId
     * @param oId
     * @param dltType
     * @return
     */
    @Override
    public ResponseResult deleteOrderByUid(Integer uId, Integer oId, Integer dltType) {
        if(dltType == 1){
            List<OrderSf> orderSfList = orderSfMapper.selectOrderSfBy(null, oId, uId, 0);
            if(orderSfList != null && orderSfList.size()>0){
                int count = orderSfMapper.deleteOrderSfBy(oId, uId);
                if(count == 1){
                    return ResponseResult.ok("");
                }
                return ResponseResult.error("-2","删除失败！");
            }
        }else if(dltType == 2){
            List<OrderPw> orderPwList = orderPwMapper.selectOrderPwBy(null, oId, uId, 0);
            if(orderPwList != null && orderPwList.size()>0){
                int count = orderPwMapper.deleteOrderPwBy(oId, uId);
                if(count == 1){
                    return ResponseResult.ok("");
                }
                return ResponseResult.error("-2","删除失败！");
            }
        }
        return ResponseResult.error("-1","订单异常！");
    }

    /**
     * 添加订单评论
     * @param orderEvaluate
     * @return
     */
    @Override
    public int insertEvaluate(OrderEvaluate orderEvaluate) {
        return orderEvaluateMapper.insertEvaluate(orderEvaluate);
    }


    @Override
    public List<OrderEvaluate> selectEvaluateBy(Integer uId, Integer oId) {
        return orderEvaluateMapper.selectEvaluateBy(uId,oId);
    }


}
