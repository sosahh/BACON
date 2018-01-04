package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserDynamic;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDynamicMapper {

    int insert(UserDynamic userDynamic);

    int updateByPrimaryKeySelective(UserDynamic userDynamic);

    int updateByPrimaryKey(UserDynamic userDynamic);
}