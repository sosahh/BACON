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
    List<UserInfo> getUserByCategoryId(@Param("categoryId")Integer categoryId,@Param("sex")Integer sex,
                                       @Param("titlePwName")String titlePwName,@Param("age")Integer age,@Param("age1")Integer age1);

    //查询明星用户
    List<UserInfo> getStarUserInfo();


}