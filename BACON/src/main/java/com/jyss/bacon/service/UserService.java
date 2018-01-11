package com.jyss.bacon.service;


import com.jyss.bacon.entity.ResponseResult;
import com.jyss.bacon.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {

    //用户注册
    ResponseResult insert(User user);

    //用户登陆
    ResponseResult getUser(@Param("tel")String tel, @Param("password")String password);

    //用户资料查询
    ResponseResult getUserInfo(@Param("uId")String uId);

    //条件查询用户
    List<User> selectUserBy(@Param("uId")String uId, @Param("tel")String tel, @Param("status")String status);

    //修用户信息修改
    int updateUser(User user);

}
