package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserDynamic;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDynamicMapper {

    int insert(UserDynamic userDynamic);

    int updateByPrimaryKeySelective(UserDynamic userDynamic);

    int updateByPrimaryKey(UserDynamic userDynamic);

    //条件查询
    List<UserDynamic> getUserUserDynamicBy(@Param("uId")Integer uId,@Param("status")Integer status);

}