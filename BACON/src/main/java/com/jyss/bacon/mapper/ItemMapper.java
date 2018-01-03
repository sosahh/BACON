package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemMapper {

    int insert(Item item);

    int updateByPrimaryKeySelective(Item item);

    int updateByPrimaryKey(Item item);
}