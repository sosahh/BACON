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
        Map<String, Object> map = new HashMap<>();
        List<Xtcl> xtclList = xtclMapper.getClsBy("condition_type", null);

        //查询年龄
        Xtcl xtcl1 = xtclMapper.getClsValue("age_type", "1");
        String age = xtcl1.getBz_value();                                          //22
        String info = xtcl1.getBz_info();                                          //22岁以下
        Xtcl xtcl2 = xtclMapper.getClsValue("age_type", "2");
        String age1 = xtcl2.getBz_value();                                         //25
        String info1 = xtcl2.getBz_info();                                          //25岁以上

        List<Object> list1 = new ArrayList<>();
        for (Xtcl xtcl : xtclList) {
            if(xtcl.getBz_id().equals("1")){
                List<Condition.ItemsBean> list = new ArrayList<>();
                Condition.ItemsBean itemsBean = new Condition.ItemsBean();
                itemsBean.setId("0");
                itemsBean.setName("全部");
                Condition.ItemsBean itemsBean1 = new Condition.ItemsBean();
                itemsBean1.setId("1");
                itemsBean1.setName("女生");
                Condition.ItemsBean itemsBean2 = new Condition.ItemsBean();
                itemsBean2.setId("2");
                itemsBean2.setName("男生");
                list.add(itemsBean);
                list.add(itemsBean1);
                list.add(itemsBean2);

                Condition condition = new Condition();
                condition.setTitle("性别");
                condition.setItems(list);
                list1.add(condition);

            }
            if(xtcl.getBz_id().equals("2")){
                Condition condition = new Condition();
                List<ItemCat> list = itemCatMapper.selectDwNameByCategoryId(categoryId);
                List<Condition.ItemsBean> list2 = new ArrayList<>();
                Condition.ItemsBean itemsBean1 = new Condition.ItemsBean();
                itemsBean1.setId("全部");
                itemsBean1.setName("全部");
                list2.add(itemsBean1);
                for (ItemCat itemCat : list) {
                    Condition.ItemsBean itemsBean = new Condition.ItemsBean();
                    itemsBean.setId(itemCat.getDwName());
                    itemsBean.setName(itemCat.getDwName());
                    list2.add(itemsBean);
                }
                condition.setTitle("等级");
                condition.setItems(list2);
                list1.add(condition);
            }
            if(xtcl.getBz_id().equals("3")){
                List<Condition.ItemsBean> list = new ArrayList<>();
                Condition.ItemsBean itemsBean = new Condition.ItemsBean();
                itemsBean.setId("10");
                itemsBean.setName("全部");
                Condition.ItemsBean itemsBean1 = new Condition.ItemsBean();
                itemsBean1.setId("11");
                itemsBean1.setName(info);
                Condition.ItemsBean itemsBean2 = new Condition.ItemsBean();
                itemsBean2.setId("12");
                itemsBean2.setName(age+"-"+age1);
                Condition.ItemsBean itemsBean3 = new Condition.ItemsBean();
                itemsBean3.setId("13");
                itemsBean3.setName(info1);

                list.add(itemsBean);
                list.add(itemsBean1);
                list.add(itemsBean2);
                list.add(itemsBean3);

                Condition condition = new Condition();
                condition.setTitle("年龄段");
                condition.setItems(list);
                list1.add(condition);

            }
        }
        map.put("condition",list1);
        return ResponseResult.ok(map);
    }

    /**
     * 查询所有小段位
     */
    @Override
    public List<ItemCat> getItemCatBy(Integer categoryId, String dwName, Integer status) {
        return itemCatMapper.getItemCatBy(categoryId,dwName,status);
    }


    /**
     * 通过id查询小段位
     * @param id
     * @return
     */
    @Override
    public ItemCat getItemCatById(Integer id) {
        return itemCatMapper.getItemCatById(id);
    }


}
