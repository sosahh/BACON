package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserAuth;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthMapper {

    int insert(UserAuth userAuth);

    int updateByPrimaryKeySelective(UserAuth userAuth);

    int updateByPrimaryKey(UserAuth userAuth);
}