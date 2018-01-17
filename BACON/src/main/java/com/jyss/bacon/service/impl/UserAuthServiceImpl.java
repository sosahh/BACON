package com.jyss.bacon.service.impl;

import com.jyss.bacon.entity.UserAuth;
import com.jyss.bacon.mapper.UserAuthMapper;
import com.jyss.bacon.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserAuthServiceImpl implements UserAuthService{
    @Autowired
    private UserAuthMapper userAuthMapper;


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
}
