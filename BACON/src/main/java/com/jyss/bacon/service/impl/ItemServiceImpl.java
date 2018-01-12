package com.jyss.bacon.service.impl;

import com.jyss.bacon.entity.BaseNew;
import com.jyss.bacon.entity.Item;
import com.jyss.bacon.entity.ItemCat;
import com.jyss.bacon.mapper.BaseNewMapper;
import com.jyss.bacon.mapper.ItemCatMapper;
import com.jyss.bacon.mapper.ItemMapper;
import com.jyss.bacon.mapper.UserInfoMapper;
import com.jyss.bacon.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService{

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private BaseNewMapper baseNewMapper;


    /**
     * 查询类目
     */
    @Override
    public List<Item> selectItem() {
        return itemMapper.selectItem();
    }

    /**
     * 查询所有大段位
     */
    @Override
    public List<ItemCat> selectDwNameByCategoryId(Integer categoryId) {
        return itemCatMapper.selectDwNameByCategoryId(categoryId);
    }

    /**
     * 查询所有新闻
     */
    @Override
    public List<BaseNew> getAllNews() {
        return baseNewMapper.getAllNews();
    }


}
