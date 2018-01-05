package com.jyss.bacon.service.impl;

import com.jyss.bacon.entity.Item;
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
    private UserInfoMapper userInfoMapper;


    /**
     * 查询类目
     */
    @Override
    public List<Item> selectItem() {
        return itemMapper.selectItem();
    }


}
