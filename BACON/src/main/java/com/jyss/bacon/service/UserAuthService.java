package com.jyss.bacon.service;


import com.jyss.bacon.entity.UserAuth;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAuthService {

    //条件查询
    List<UserAuth> getUserAuthBy(@Param("uId")Integer uId, @Param("categoryId")Integer categoryId, @Param("status")Integer status);
}
