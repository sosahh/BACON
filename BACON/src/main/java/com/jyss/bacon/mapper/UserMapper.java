package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    int insert(User user);

    int updateByPrimaryKeySelective(User user);

    int updateByPrimaryKey(User user);

    //条件查询用户
    List<User> selectUserBy(@Param("id")String id,@Param("tel")String tel,@Param("status")String status);
}