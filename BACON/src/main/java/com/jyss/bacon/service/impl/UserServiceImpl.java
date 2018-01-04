package com.jyss.bacon.service.impl;

import com.jyss.bacon.entity.User;
import com.jyss.bacon.mapper.UserMapper;
import com.jyss.bacon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectUserBy(String id, String tel, String status) {
        return userMapper.selectUserBy(id,tel,status);
    }
}
