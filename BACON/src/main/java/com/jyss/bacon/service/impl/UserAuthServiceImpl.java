package com.jyss.bacon.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.bacon.entity.OrderEvaluate;
import com.jyss.bacon.entity.Page;
import com.jyss.bacon.entity.ResponseResult;
import com.jyss.bacon.entity.UserAuth;
import com.jyss.bacon.mapper.OrderEvaluateMapper;
import com.jyss.bacon.mapper.OrderPwMapper;
import com.jyss.bacon.mapper.UserAuthMapper;
import com.jyss.bacon.service.UserAuthService;
import com.jyss.bacon.utils.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class UserAuthServiceImpl implements UserAuthService{
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private OrderPwMapper orderPwMapper;
    @Autowired
    private OrderEvaluateMapper orderEvaluateMapper;


    @Override
    public int insert(UserAuth userAuth) {
        return userAuthMapper.insert(userAuth);
    }

    @Override
    public int updateByPrimaryKeySelective(UserAuth userAuth) {
        return userAuthMapper.updateByPrimaryKeySelective(userAuth);
    }

    @Override
    public List<UserAuth> getUserAuthBy(Integer uId, Integer categoryId, Integer status) {
        return userAuthMapper.getUserAuthBy(uId,categoryId,status);
    }


    /**
     * 游戏详细信息
     * @param authId
     * @return
     */
    @Override
    public ResponseResult selectUserAuth(Integer uId,Integer authId,Integer page,Integer pageSize) {
        List<UserAuth> userAuthList = userAuthMapper.getUserAuthById(authId, 2);
        if(userAuthList != null && userAuthList.size()==1){
            HashMap<String, Object> map = new HashMap<>();
            UserAuth userAuth = userAuthList.get(0);
            int count1 = orderPwMapper.selectCount(userAuth.getuId(), userAuth.getCategoryId());
            int count2 = orderEvaluateMapper.selectCountEvaluate(userAuth.getuId(), userAuth.getCategoryId());

            PageHelper.startPage(page,pageSize);
            List<OrderEvaluate> list = orderEvaluateMapper.selectOrderEvaluateBy(userAuth.getuId(), userAuth.getCategoryId());
            for (OrderEvaluate orderEvaluate : list) {
                orderEvaluate.setShowTime(DateFormatUtils.showTimeText(orderEvaluate.getCreated()));
            }
            PageInfo<OrderEvaluate> pageInfo = new PageInfo<>(list);
            Page<OrderEvaluate> result = new Page<>(pageInfo);
            if(uId == null){
                map.put("uId",0);
            }else{
                map.put("uId",uId);
            }
            map.put("game",userAuth);
            map.put("orders",count1);
            map.put("evaluates",count2);
            map.put("items",result);
            return ResponseResult.ok(map);
        }
        return ResponseResult.error("-1","查询失败！");
    }
}
