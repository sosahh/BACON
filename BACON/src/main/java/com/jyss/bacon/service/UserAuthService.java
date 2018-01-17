package com.jyss.bacon.service;


import com.jyss.bacon.entity.UserAuth;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAuthService {

    //添加认证
    int insert(UserAuth userAuth);

    //修改认证游戏
    int updateByPrimaryKeySelective(UserAuth userAuth);

    //条件查询
    List<UserAuth> getUserAuthBy(@Param("uId")Integer uId, @Param("categoryId")Integer categoryId, @Param("status")Integer status);
}
