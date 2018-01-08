package com.jyss.bacon.service;


import com.jyss.bacon.entity.Page;
import com.jyss.bacon.entity.User;
import org.apache.ibatis.annotations.Param;


public interface UserFellowService {

    //点击关注
    int insertUserFellow(@Param("uId")Integer uId,@Param("fellowId")Integer fellowId);

    //取消关注
    int deleteUserFellow(@Param("uId")Integer uId,@Param("followId")Integer followId);

    //查询我的关注
    Page<User> getUserFellowById(@Param("uId")Integer uId, @Param("page")Integer page, @Param("limit")Integer pageSize);

    //查询关注我的
    Page<User> getUserFellowByFellowId(@Param("uId")Integer uId,@Param("page")Integer page, @Param("limit")Integer pageSize);

}
