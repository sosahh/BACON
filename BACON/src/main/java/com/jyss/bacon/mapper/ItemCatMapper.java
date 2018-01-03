package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.ItemCat;


public interface  ItemCatMapper {

    int insert(ItemCat itemCat);

    int updateByPrimaryKeySelective(ItemCat itemCat);

    int updateByPrimaryKey(ItemCat itemCat);
}