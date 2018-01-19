package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.MobileLogin;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MobileLoginMapper {

    //添加登陆记录
    int insert(MobileLogin mobileLogin);

    int updateByPrimaryKeySelective(MobileLogin mobileLogin);

    //根据token查询用户
    List<MobileLogin> findUserByToken(@Param("token")String token);
}