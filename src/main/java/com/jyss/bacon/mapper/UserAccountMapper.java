package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountMapper {

    int insertUserAccount(UserAccount userAccount);

    //查询账户
    List<UserAccount> getUserAccount(@Param("uId")Integer uId,@Param("account")String account);

    //更新账户
    int updateByPrimaryKeySelective(UserAccount userAccount);




}