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
    List<UserDynamic> getUserDynamicBy(@Param("uId")Integer uId,@Param("status")Integer status);

    //条件查询动态
    List<UserDynamic> selectUserDynamicBy(@Param("uId")Integer uId,@Param("sex")Integer sex);

    //查询关注人的动态
    List<UserDynamic> getDynamicByFellowId(@Param("uId")Integer uId);

    //删除动态
    int deleteUserDynamicById(@Param("id")Integer id);

}