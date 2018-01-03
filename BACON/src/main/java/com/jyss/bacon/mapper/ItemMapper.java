package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.Item;


public interface ItemMapper {

    int insert(Item item);

    int updateByPrimaryKeySelective(Item item);

    int updateByPrimaryKey(Item item);
}