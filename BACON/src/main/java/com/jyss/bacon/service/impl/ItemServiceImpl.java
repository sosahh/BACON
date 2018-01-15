package com.jyss.bacon.service.impl;

import com.jyss.bacon.entity.*;
import com.jyss.bacon.mapper.*;
import com.jyss.bacon.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ItemServiceImpl implements ItemService{

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private BaseNewMapper baseNewMapper;
    @Autowired
    private XtclMapper xtclMapper;


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


    /**
     * 条件查询
     * @param id
     * @return
     */
    @Override
    public List<BaseNew> selectBaseNewBy(Integer id) {
        return baseNewMapper.selectBaseNewBy(id);
    }


    /**
     * 条件筛选
     * @return
     */
    @Override
    public ResponseResult getCondition(Integer categoryId) {
        Map<String, Object> map = new LinkedHashMap<>();
        List<Xtcl> xtclList = xtclMapper.getClsBy("condition_type", null);

        //查询年龄
        Xtcl xtcl1 = xtclMapper.getClsValue("age_type", "1");
        String age = xtcl1.getBz_value();                                          //22
        String info = xtcl1.getBz_info();                                          //22岁以下
        Xtcl xtcl2 = xtclMapper.getClsValue("age_type", "2");
        String age1 = xtcl2.getBz_value();                                         //25
        String info1 = xtcl2.getBz_info();                                          //25岁以上

        for (Xtcl xtcl : xtclList) {
            if(xtcl.getBz_id().equals("1")){
                Map<String, String> map1 = new LinkedHashMap<>();
                map1.put("1","女生");
                map1.put("2","男生");
                map.put("sex",map1);
            }
            if(xtcl.getBz_id().equals("2")){
                List<ItemCat> list = itemCatMapper.selectDwNameByCategoryId(categoryId);
                map.put("level",list);
            }
            if(xtcl.getBz_id().equals("3")){
                Map<String, String> map1 = new LinkedHashMap<>();
                map1.put("1",info);
                map1.put("2",age+"-"+age1);
                map1.put("3",info1);
                map.put("age",map1);
            }
        }
        return ResponseResult.ok(map);
    }


}
