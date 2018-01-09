package com.jyss.bacon.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.bacon.entity.Page;
import com.jyss.bacon.entity.User;
import com.jyss.bacon.entity.UserFollow;
import com.jyss.bacon.mapper.UserFollowMapper;
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
        int gzId = userFollowMapper.insert(userFollow);
        return gzId;
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
    public Page<User> getUserFellowById(Integer uId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<User> userList = userMapper.getUserFellowByUid(uId);
        for (User user : userList) {
            List<UserFollow> followList = userFollowMapper.getUserFellowBy(user.getuId(), uId, 1);
            if(followList != null && followList.size()>0){
                user.setStatus(1);
            }else {
                user.setStatus(0);
            }
        }
        PageInfo<User> pageInfo = new PageInfo<>(userList);

        Page<User> result = new Page<>();
        result.setTotal(pageInfo.getTotal());
        result.setNext(pageInfo.isHasNextPage());
        result.setRows(userList);
        return result;
    }

    /**
     * 查询关注我的   status: 0 = 已关注,1 = 互相关注
     */
    @Override
    public Page<User> getUserFellowByFellowId(Integer uId,Integer page,Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<User> userList = userMapper.getUserFellowByFellowId(uId);
        for (User user : userList) {
            List<UserFollow> followList = userFollowMapper.getUserFellowBy(uId, user.getuId(), 1);
            if(followList != null && followList.size()>0){
                user.setStatus(1);
            }else{
                user.setStatus(0);
            }
        }
        PageInfo<User> pageInfo = new PageInfo<>(userList);

        Page<User> result = new Page<>();
        result.setTotal(pageInfo.getTotal());
        result.setNext(pageInfo.isHasNextPage());
        result.setRows(userList);
        return result;
    }
}
