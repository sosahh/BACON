package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserPraise;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPraiseMapper {

    int insert(UserPraise userPraise);

    int updateByPrimaryKeySelective(UserPraise userPraise);

    int updateByPrimaryKey(UserPraise userPraise);
}