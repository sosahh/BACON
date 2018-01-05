package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserFollow;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowMapper {

    //点击关注
    int insert(UserFollow userFollow);

    int updateByPrimaryKeySelective(UserFollow userFollow);

    int updateByPrimaryKey(UserFollow userFollow);

    //查询总关注数
    int getUserFellowCount(@Param("followId")Integer followId);

    //条件查询
    List<UserFollow> getUserFellowBy(@Param("uId")Integer uId,@Param("followId")Integer followId,@Param("status")Integer status);

}