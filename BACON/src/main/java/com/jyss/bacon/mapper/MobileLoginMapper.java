package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.MobileLogin;

public interface MobileLoginMapper {

    int insert(MobileLogin mobileLogin);

    int updateByPrimaryKeySelective(MobileLogin mobileLogin);

    int updateByPrimaryKey(MobileLogin mobileLogin);
}