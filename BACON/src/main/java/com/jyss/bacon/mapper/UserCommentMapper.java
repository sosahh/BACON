package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserComment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCommentMapper {

    /**
     * 添加评价
     * @param userComment
     * @return
     */
    int insert(UserComment userComment);

    int updateByPrimaryKeySelective(UserComment userComment);

    int updateByPrimaryKey(UserComment userComment);

    /**
     * 动态评价查询
     * @param dynamicId
     * @return
     */
    List<UserComment> selectCommentBy(@Param("dynamicId")Integer dynamicId);

    //删除评价
    int deleteCommentBy(@Param("dynamicId")Integer dynamicId,@Param("uId")Integer uId);
}