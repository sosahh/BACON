package com.jyss.bacon.service;

import com.jyss.bacon.entity.MobileLogin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/1/3.
 */
public interface MobileLoginService {

    List<MobileLogin> findUserByToken(@Param("token") String token);
}
