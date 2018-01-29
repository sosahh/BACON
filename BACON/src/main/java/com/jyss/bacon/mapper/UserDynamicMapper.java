package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserDynamic;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDynamicMapper {

    //添加动态
    int insert(UserDynamic userDynamic);

    int updateByPrimaryKeySelective(UserDynamic userDynamic);


    //条件查询动态
    List<UserDynamic> selectUserDynamicBy(@Param("uId")Integer uId,@Param("sex")Integer sex);

    //查询关注人的动态
    List<UserDynamic> getDynamicByFellowId(@Param("uId")Integer uId);

    //删除我的动态
    int deleteUserDynamicById(@Param("id")Integer id);

    //查询8张图片
    List<UserDynamic> getPicture(@Param("uId")Integer uId);

    //举报动态查询
    List<UserDynamic> getUserDynamicById(@Param("dynamicId")Integer dynamicId);

}