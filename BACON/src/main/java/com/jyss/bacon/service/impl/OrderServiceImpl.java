package com.jyss.bacon.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.bacon.constant.Constant;
import com.jyss.bacon.entity.*;
import com.jyss.bacon.mapper.*;
import com.jyss.bacon.service.OrderService;
import com.jyss.bacon.utils.DateFormatUtils;
import jdk.nashorn.internal.objects.NativeRegExp;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static javax.xml.stream.XMLStreamConstants.COMMENT;

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
    @Autowired
    private OrderSfResultMapper orderSfResultMapper;



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
                            scoreBalance.setDetail("上分订单支付支出");
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
                    scoreBalance.setDetail("上分订单取消收入");
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
        List<OrderSf> orderSfList = orderSfMapper.getOrderSfByUid(uId,null);
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
                            List<User> list = userMapper.selectUserBy(orderPw.getPlayId() + "", null, null);
                            User user2 = list.get(0);
                            ScoreBalance scoreBalance = new ScoreBalance();
                            scoreBalance.setEnd(2);
                            scoreBalance.setuId(uId);
                            scoreBalance.setDetail(orderPw.getCategoryTitle()+"-"+user2.getNick());
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
                        List<User> list = userMapper.selectUserBy(orderPw.getPlayId() + "", null, null);
                        User user2 = list.get(0);
                        ScoreBalance scoreBalance = new ScoreBalance();
                        scoreBalance.setEnd(2);
                        scoreBalance.setuId(uId);
                        scoreBalance.setDetail(orderPw.getCategoryTitle()+"-"+user2.getNick());
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
                        List<User> list = userMapper.selectUserBy(uId + "", null, null);
                        User user2 = list.get(0);
                        ScoreBalance scoreBalance = new ScoreBalance();
                        scoreBalance.setEnd(2);
                        scoreBalance.setuId(orderPw.getuId());
                        scoreBalance.setDetail(orderPw.getCategoryTitle()+"-"+user2.getNick());
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
            double jyScore = user.getAmount() + orderPw.getTotal();
            User user1 = new User();
            user1.setuId(uId);
            user1.setAmount((float) jyScore);
            user1.setLastModifyTime(new Date());
            int count = userMapper.updateByPrimaryKeySelective(user1);
            if(count == 1){
                OrderPw orderPw1 = new OrderPw();
                orderPw1.setId(oId);
                orderPw1.setStatus(4);
                orderPw1.setModifyTime(new Date());
                int count1 = orderPwMapper.updateByPrimaryKeySelective(orderPw1);
                if(count1 == 1){
                    List<User> list = userMapper.selectUserBy(orderPw.getuId() + "", null, null);
                    User user2 = list.get(0);
                    ScoreEarn scoreEarn = new ScoreEarn();
                    scoreEarn.setuId(uId);
                    scoreEarn.setDetail(orderPw.getCategoryTitle()+"-"+user2.getNick());
                    scoreEarn.setType(1);
                    scoreEarn.setScore(orderPw.getTotal());
                    scoreEarn.setJyScore(jyScore);
                    scoreEarn.setOrderSn(orderPw.getOrderId());
                    scoreEarn.setStatus(1);
                    scoreEarn.setCreatedAt(new Date());
                    int count2 = scoreBalanceMapper.insertScoreEarn(scoreEarn);
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


    /**
     * 上分订单详情
     * @param uId
     * @param oId
     * @return
     */
    @Override
    public ResponseResult getOrderSfDetails(Integer uId, Integer oId) {
        List<OrderSf> orderSfList = orderSfMapper.getOrderSfByUid(uId,oId);
        if(orderSfList != null && orderSfList.size()==1){
            OrderSf orderSf = orderSfList.get(0);
            List<OrderSfResult> results = orderSfResultMapper.selectOrderSfResult(orderSf.getOrderId());
            if(results != null && results.size()>0){
                OrderSfResult orderSfResult = results.get(0);
                results.remove(orderSfResult);
                String picture = orderSfResult.getPicture();
                if(!StringUtils.isEmpty(picture)){
                    List<String> list = new ArrayList<>();
                    String[] pictures = picture.split(";");
                    for (String pic : pictures) {
                        list.add(Constant.httpUrl+pic);
                    }
                    orderSfResult.setPictures(list);
                }
                results.add(orderSfResult);
            }
            Map<Object, Object> map = new HashMap<>();
            map.put("order",orderSf);
            map.put("result",results);
            return ResponseResult.ok(map);
        }

        return ResponseResult.error("-1","订单异常！");
    }


    /**
     * 陪玩订单详情    1=用户端，2=陪玩人端
     * @param uId
     * @param oId
     * @return
     */
    @Override
    public ResponseResult getOrderPwDetails(Integer uId, Integer oId, Integer pwType) {
        if(pwType == 1){
            List<OrderPw> orderPwList = orderPwMapper.selectOrderPwBy(null, oId, uId, null);
            if(orderPwList != null && orderPwList.size()==1){
                OrderPw orderPw = orderPwList.get(0);
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

                HashMap<String, Object> map = new HashMap<>();
                List<User> userList = userMapper.selectUserBy(orderPw.getPlayId() + "", null, null);
                User user = userList.get(0);
                map.put("order",orderPw);
                map.put("user",user);
                return ResponseResult.ok(map);
            }

        }else if(pwType == 2){
            List<OrderPw> orderPwList = orderPwMapper.selectOrderPwBy(uId, oId, null, null);
            if(orderPwList != null && orderPwList.size()==1){
                OrderPw orderPw = orderPwList.get(0);
                HashMap<String, Object> map = new HashMap<>();
                List<User> userList = userMapper.selectUserBy(orderPw.getuId() + "", null, null);
                User user = userList.get(0);
                map.put("order",orderPw);
                map.put("user",user);
                return ResponseResult.ok(map);
            }
        }

        return ResponseResult.error("-1","订单异常！");
    }

    @Override
    public OrderSfResult getOrderSfSumTotal(@Param("sfUserId") String sfUserId) {
        return orderSfResultMapper.getOrderSfSumTotal(sfUserId);
    }

    /**
     *  上分订单结果获得总金额[订单金额，实得金额]
     *status = 0未支付，1已支付，2已接单，3完成，4订单取消
     *reStatus=1 =分配订单 2=完成订单 3=取消订单
     * @param sfUserId
     * @param status
     * @param reStatus
     * @return
     */
    @Override
    public OrderSfView getSfOrderSumTotal(@Param("sfUserId") String sfUserId, @Param("status") String status, @Param("reStatus") String reStatus) {
        return orderSfResultMapper.getSfOrderSumTotal(sfUserId, status, reStatus);
    }


    /**
     *  上分订单结果详情查询
     *status = 0未支付，1已支付，2已接单，3完成，4订单取消
     *reStatus=1 =分配订单 2=完成订单 3=取消订单
     * @param sfUserId
     * @param status
     * @param reStatus
     * @return
     */
    @Override
    public List<OrderSfView> getSfOrderResultInfo(@Param("sfUserId") String sfUserId, @Param("status") String status, @Param("reStatus") String reStatus) {
        List<OrderSfView> list = orderSfResultMapper.getSfOrderResultInfo(sfUserId, status, reStatus);
        if (list!=null&&list.size()>0){
            for(OrderSfView os :list){
                os.setHeadpic(Constant.httpUrl+os.getHeadpic());
                String strImg = os.getPicture();
                if (strImg!=null&&!(strImg.equals(""))){
                    String [] strarr = strImg.split(";");
                    if (strarr!=null){
                        for(int i =0; i<strarr.length;i++){
                            strarr[i] = Constant.httpUrl+strarr[i];
                        }
                        os.setPictures(strarr);
                    }
                }

            }

        }
        return list;
    }

    /**
     * 修改订单表状态
     * @param orderId
     * @param status
     * @param statusBefore
     * @return
     */
    @Override
    public int upOrderSf(@Param("orderId") String orderId, @Param("status") String status, @Param("statusBefore") String statusBefore) {
        return orderSfResultMapper.upOrderSf(orderId, status, statusBefore);
    }

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
    @Override
    public int upOrderSfResult(@Param("orderId") String orderId, @Param("sfStar") String sfStar, @Param("result") String result, @Param("pictures") String pictures, @Param("status") String status, @Param("statusBefore") String statusBefore) {
        return orderSfResultMapper.upOrderSfResult(orderId, sfStar, result, pictures,status, statusBefore );
    }


    ////// *status = 0未支付，1已支付，2已接单，3完成，4订单取消
    ////// *reStatus=1 =分配订单 2=完成订单 3=取消订单
    @Override
    public int updateMyOrderResult(OrderSfResult os,double balance) {
        int count = 0;
        ////先修改状态
        count = upOrderSf(os.getOrderId(),"3","2");
        if (count==1){
            count = 0;
            count = upOrderSfResult(os.getOrderId(),os.getSfStar(),os.getResult(),os.getPicture(),"2","1");
            if (count==1){
                count = userMapper.upUserSfBalance(os.getSfUserId().toString(),balance+"");
                return count;
            }
            return count;
        }
        return 0;
    }

    @Override
    public int insertDlScoreEarn(DlAppEarn dlAppEarn) {
        dlAppEarn.setType(2);
        dlAppEarn.setDetail("取现");
        dlAppEarn.setStatus(0);///0==等待提现审核 1=提现成功 2-提现失败
        return orderSfResultMapper.insertScoreEarn(dlAppEarn);
    }

    @Override
    public int addDlScoreEarn(DlAppEarn dlAppEarn, double balance) {
        int count = userMapper.upUserSfBalance(dlAppEarn.getuId().toString(),balance+"");
        if (count==1){
            count = insertDlScoreEarn(dlAppEarn);
            return count;
        }
        return 0;
    }

    @Override
    public List<DrawCashDetails> getDrawCashDetails(@Param("uid") String uid) {
        return orderSfResultMapper.getDrawCashDetails(uid);
    }

    @Override
    public List<OrderSfResult> getResultInfo(@Param("sfUserId") String sfUserId, @Param("orderId") String orderId, @Param("status") String status) {
        return orderSfResultMapper.getResultInfo(sfUserId,orderId,status);
    }


}
