package com.jyss.bacon.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.bacon.entity.Page;
import com.jyss.bacon.entity.UserFollow;
import com.jyss.bacon.entity.UserInfo;
import com.jyss.bacon.mapper.UserFollowMapper;
import com.jyss.bacon.mapper.UserInfoMapper;
import com.jyss.bacon.mapper.UserMapper;
import com.jyss.bacon.service.UserFellowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserFellowServiceImpl implements UserFellowService{
    @Autowired
    private UserFollowMapper userFollowMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 点击关注
     */
    @Override
    public int insertUserFellow(Integer uId, Integer fellowId) {
        UserFollow userFollow = new UserFollow();
        userFollow.setuId(uId);
        userFollow.setFollowId(fellowId);
        userFollow.setStatus(1);
        userFollow.setCreated(new Date());
        int count = userFollowMapper.insert(userFollow);
        return count;
    }

    /**
     * 取消关注
     */
    @Override
    public int deleteUserFellow(Integer uId, Integer followId) {
        return userFollowMapper.deleteUserFellow(uId,followId);
    }

    /**
     * 查询我的关注   status: 0 = 已关注,1 = 互相关注
     */
    @Override
    public Page<UserInfo> getUserFellowById(Integer uId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<UserInfo> list = userInfoMapper.getUserFellowByUid(uId);
        for (UserInfo userInfo : list) {
            List<UserFollow> followList = userFollowMapper.getUserFellowBy(userInfo.getuId(), uId, 1);
            if(followList != null && followList.size()>0){
                userInfo.setStatus(1);
            }else {
                userInfo.setStatus(0);
            }
        }
        PageInfo<UserInfo> pageInfo = new PageInfo<>(list);

        Page<UserInfo> result = new Page<>();
        result.setTotal(pageInfo.getTotal());
        result.setNext(pageInfo.isHasNextPage());
        result.setRows(list);
        return result;
    }

    /**
     * 查询关注我的   status: 0 = 已关注,1 = 互相关注
     */
    @Override
    public Page<UserInfo> getUserFellowByFellowId(Integer uId,Integer page,Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<UserInfo> list = userInfoMapper.getUserFellowByFellowId(uId);
        for (UserInfo userInfo : list) {
            List<UserFollow> followList = userFollowMapper.getUserFellowBy(uId, userInfo.getuId(), 1);
            if(followList != null && followList.size()>0){
                userInfo.setStatus(1);
            }else{
                userInfo.setStatus(0);
            }
        }
        PageInfo<UserInfo> pageInfo = new PageInfo<>(list);

        Page<UserInfo> result = new Page<>();
        result.setTotal(pageInfo.getTotal());
        result.setNext(pageInfo.isHasNextPage());
        result.setRows(list);
        return result;
    }
}
