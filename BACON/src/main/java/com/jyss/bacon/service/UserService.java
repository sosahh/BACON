package com.jyss.bacon.service;


import com.jyss.bacon.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {

    //条件查询用户
    List<User> selectUserBy(@Param("id")String id, @Param("tel")String tel, @Param("status")String status);
}
