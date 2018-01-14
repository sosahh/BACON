package com.jyss.bacon.service;


import com.jyss.bacon.entity.BaseNew;
import com.jyss.bacon.entity.Item;
import com.jyss.bacon.entity.ItemCat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemService {

    //查询所有类目
    List<Item> selectItem();

    //查询所有大段位
    List<ItemCat> selectDwNameByCategoryId(@Param("categoryId")Integer categoryId);

    //查询所有新闻
    List<BaseNew> getAllNews();

    //条件查询
    List<BaseNew> selectBaseNewBy(@Param("id")Integer id);
}
