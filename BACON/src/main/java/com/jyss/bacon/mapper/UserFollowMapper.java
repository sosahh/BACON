package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserFollow;


public interface UserFollowMapper {

    int insert(UserFollow userFollow);

    int updateByPrimaryKeySelective(UserFollow userFollow);

    int updateByPrimaryKey(UserFollow userFollow);
}