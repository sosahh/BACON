package com.jyss.bacon.service;

import com.jyss.bacon.entity.MobileLogin;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface MobileLoginService {

    //添加登陆记录
    int insert(MobileLogin mobileLogin);

    List<MobileLogin> findUserByToken(@Param("token") String token);
}
