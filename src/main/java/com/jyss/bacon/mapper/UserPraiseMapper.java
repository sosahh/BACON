package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserPraise;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPraiseMapper {

    //点赞
    int insert(UserPraise userPraise);

    int updateByPrimaryKeySelective(UserPraise userPraise);

    //条件查询
    List<UserPraise> getUserPraiseBy(@Param("dynamicId")Integer dynamicId,@Param("uId")Integer uId,@Param("status")Integer status);

    //查询点赞数
    long getCountPraise(@Param("dynamicId")Integer dynamicId);

    //取消点赞
    int deletePraiseBy(@Param("dynamicId")Integer dynamicId,@Param("uId")Integer uId);

    //取消新闻点赞
    int deleteNewPraiseBy(@Param("dynamicId")Integer dynamicId,@Param("uId")Integer uId);

}