package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserInfoMapper {

    //用户名或id搜索
    List<UserInfo> getUserByNickOrAccount(@Param("account")String account);

    //根据条件查询
    List<UserInfo> getUserInfoBy(@Param("categoryId")Integer categoryId,@Param("sex")Integer sex,
                                       @Param("titlePwName")String titlePwName,@Param("age")Integer age,@Param("age1")Integer age1);

    //查询明星用户
    List<UserInfo> getStarUserInfo();

    //查询热门用户
    List<UserInfo> getHotUserInfo();

    //查询新人用户
    List<UserInfo> getNewUserInfo();

    //查询我的关注
    List<UserInfo> getUserFellowByUid(@Param("uId")Integer uId);

    //查询关注我的
    List<UserInfo> getUserFellowByFellowId(@Param("followId")Integer followId);


}