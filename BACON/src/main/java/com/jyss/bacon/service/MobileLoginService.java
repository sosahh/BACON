package com.jyss.bacon.service;

import com.jyss.bacon.entity.MobileLogin;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface MobileLoginService {

    List<MobileLogin> findUserByToken(@Param("token") String token);
}
