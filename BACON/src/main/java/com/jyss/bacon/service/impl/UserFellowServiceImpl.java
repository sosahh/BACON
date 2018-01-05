package com.jyss.bacon.service.impl;

import com.jyss.bacon.entity.UserFollow;
import com.jyss.bacon.mapper.UserFollowMapper;
import com.jyss.bacon.service.UserFellowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class UserFellowServiceImpl implements UserFellowService{
    @Autowired
    private UserFollowMapper userFollowMapper;

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



}
