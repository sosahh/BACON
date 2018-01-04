package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserComment;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCommentMapper {

    int insert(UserComment userComment);

    int updateByPrimaryKeySelective(UserComment userComment);

    int updateByPrimaryKey(UserComment userComment);
}