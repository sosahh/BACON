package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserFollow;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFollowMapper {

    int insert(UserFollow userFollow);

    int updateByPrimaryKeySelective(UserFollow userFollow);

    int updateByPrimaryKey(UserFollow userFollow);
}