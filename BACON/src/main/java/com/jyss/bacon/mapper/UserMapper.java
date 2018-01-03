package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    int insert(User user);

    int updateByPrimaryKeySelective(User user);

    int updateByPrimaryKey(User user);
}