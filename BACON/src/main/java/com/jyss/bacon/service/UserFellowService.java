package com.jyss.bacon.service;


import org.apache.ibatis.annotations.Param;

public interface UserFellowService {

    int insertUserFellow(@Param("uId")Integer uId,@Param("fellowId")Integer fellowId);

}
