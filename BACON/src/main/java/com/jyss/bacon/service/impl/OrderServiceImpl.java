package com.jyss.bacon.service.impl;

import com.jyss.bacon.entity.OrderSf;
import com.jyss.bacon.entity.ResponseResult;
import com.jyss.bacon.entity.ScoreBalance;
import com.jyss.bacon.entity.User;
import com.jyss.bacon.mapper.OrderSfMapper;
import com.jyss.bacon.mapper.ScoreBalanceMapper;
import com.jyss.bacon.mapper.UserMapper;
import com.jyss.bacon.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseResult updateOrderSf(Integer uId, Integer oId) {
        List<User> userList = userMapper.selectUserBy(uId + "", null, null);
        User user = userList.get(0);
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


}
