package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserAuth;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAuthMapper {

    /**
     * 添加认证
     * @param userAuth
     * @return
     */
    int insert(UserAuth userAuth);

    /**
     * 修改认证游戏
     * @param userAuth
     * @return
     */
    int updateByPrimaryKeySelective(UserAuth userAuth);


    //条件查询
    List<UserAuth> getUserAuthBy(@Param("uId")Integer uId,@Param("categoryId")Integer categoryId,@Param("status")Integer status);


}