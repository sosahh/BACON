package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.User;
import com.jyss.bacon.entity.UserSf;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    //添加用户
    int insert(User user);

    //用户信息修改
    int updateByPrimaryKeySelective(User user);

    //条件查询用户
    List<User> selectUserBy(@Param("uId")String uId,@Param("tel")String tel,@Param("status")String status);

    //网页账号查询用户
    List<User> selectUserByAccountWy(@Param("accountWy")String accountWy);

    //第三方登陆查询
    List<User> selectUserByOpenId(@Param("openId")String openId, @Param("unionId")String unionId);

    //查询我的关注
    //List<User> getUserFellowByUid(@Param("uId")Integer uId);

    //查询关注我的
    //List<User> getUserFellowByFellowId(@Param("followId")Integer followId);

    //上分人员查询
    List<UserSf> selectUserSfBy(@Param("id")String id, @Param("account")String account, @Param("status")String status);

    //上分人员信息修改
    int upUserSf(UserSf userSf);


}