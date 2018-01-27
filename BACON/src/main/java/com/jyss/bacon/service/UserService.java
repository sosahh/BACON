package com.jyss.bacon.service;


import com.jyss.bacon.entity.ResponseResult;
import com.jyss.bacon.entity.User;
import com.jyss.bacon.entity.UserAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {

    //用户注册
    ResponseResult insert(User user);

    //用户登陆
    ResponseResult getUser(@Param("tel")String tel, @Param("password")String password);

    //用户第三方登陆
    ResponseResult getUserByOpenId(@Param("openId")String openId, @Param("unionId")String unionId);

    //用户资料查询
    ResponseResult getUserInfo(@Param("uId")String uId);

    //条件查询用户
    List<User> selectUserBy(@Param("uId")String uId, @Param("tel")String tel, @Param("status")String status);

    //用户信息修改
    int updateUser(User user);

    //我的收入
    ResponseResult selectUserWallet(@Param("uId")Integer uId);

    //我的培根币账单
    ResponseResult selectScoreBalance(@Param("uId")Integer uId,@Param("page")Integer page,@Param("pageSize")Integer pageSize);

    //收入账单
    ResponseResult selectMoneyDetail(@Param("uId")Integer uId,@Param("page")Integer page,@Param("pageSize")Integer pageSize);

    //我的培根币
    ResponseResult selectUserBalance(@Param("uId")Integer uId);

    //添加账户
    int insertUserAccount(UserAccount userAccount);

    //查询账户
    List<UserAccount> getUserAccount(@Param("uId")Integer uId,@Param("account")String account);

    //更新账户
    int updateUserAccount(UserAccount userAccount);

}
