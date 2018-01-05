package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserFollow;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFollowMapper {

    //点击关注
    int insert(UserFollow userFollow);

    int updateByPrimaryKeySelective(UserFollow userFollow);

    int updateByPrimaryKey(UserFollow userFollow);
}