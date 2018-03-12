package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemMapper {

    int insert(Item item);

    int updateByPrimaryKeySelective(Item item);

    //查询所有类目
    List<Item> selectItem();
}