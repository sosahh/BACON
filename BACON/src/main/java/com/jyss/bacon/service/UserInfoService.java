package com.jyss.bacon.service;


import com.jyss.bacon.entity.Page;
import com.jyss.bacon.entity.UserDetailResult;
import com.jyss.bacon.entity.UserInfo;
import org.apache.ibatis.annotations.Param;


public interface UserInfoService {

    //用户名或id搜索
    Page<UserInfo> getUserByNickOrAccount(@Param("account")String account,@Param("page")Integer page,@Param("pageSize")Integer pageSize);

    //根据类目查询
    Page<UserInfo> getUserByCategoryId(@Param("categoryId")Integer categoryId,@Param("page")Integer page, @Param("pageSize")Integer pageSize);

    //根据条件查询
    Page<UserInfo> getUserInfoBy(@Param("categoryId")Integer categoryId,@Param("sex")Integer sex,
                                 @Param("titlePwName")String titlePwName,@Param("type")String type,
                                 @Param("page")Integer page, @Param("limit")Integer pageSize);

    //查询明星用户
    Page<UserInfo> getStarUserInfo(@Param("page")Integer page, @Param("limit")Integer pageSize);


    //查询热门用户
    Page<UserInfo> getHotUserInfo(@Param("page")Integer page, @Param("limit")Integer pageSize);

    //查询详细信息
    UserDetailResult findUserDetailInfo(@Param("uId")Integer uId,@Param("playId")Integer playId);



}
