package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserAuth;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAuthMapper {

    int insert(UserAuth userAuth);

    int updateByPrimaryKeySelective(UserAuth userAuth);

    int updateByPrimaryKey(UserAuth userAuth);

    //条件查询
    List<UserAuth> getUserAuthBy(@Param("uId")Integer uId,@Param("categoryId")Integer categoryId,@Param("status")Integer status);
}