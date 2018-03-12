package com.jyss.bacon.service.impl;

import com.jyss.bacon.entity.*;
import com.jyss.bacon.mapper.*;
import com.jyss.bacon.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    @Autowired
    private UserCommentMapper userCommentMapper;
    @Autowired
    private UserPraiseMapper userPraiseMapper;
    @Autowired
    private UserReportMapper userReportMapper;
    @Autowired
    private BaseConfigMapper baseConfigMapper;


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
     * 条件查询段位
     */
    @Override
    public List<ItemCat> getItemCatBy(Integer categoryId, String dwName, Integer status) {
        return itemCatMapper.getItemCatBy(categoryId,dwName,status);
    }

    /**
     * 查询所有新闻
     */
    @Override
    public List<BaseNew> getAllNews() {
        return baseNewMapper.getAllNews();
    }


    /**
     * 条件查询新闻
     * @param newId
     * @return
     */
    @Override
    public ResponseResult selectBaseNewBy(Integer newId,Integer uId) {
        List<BaseNew> list = baseNewMapper.selectBaseNewBy(newId);
        if(list != null && list.size()>0){
            BaseNew baseNew = list.get(0);
            Map<String, Object> map = new LinkedHashMap<>();
            long count = userCommentMapper.getCountComment(newId, 2);
            if(StringUtils.isEmpty(uId)){
                map.put("status",false);
            }else{
                List<UserPraise> praiseList = userPraiseMapper.getUserPraiseBy(newId, uId, 2);
                if(praiseList != null && praiseList.size()>0){
                    map.put("status",true);
                }else{
                    map.put("status",false);
                }
            }

            map.put("baseNew",baseNew);
            map.put("count",count);
            return ResponseResult.ok(map);
        }
        return ResponseResult.error("-1","查询无结果！");
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
        String info = xtcl1.getPs();                                               //22岁以下
        Xtcl xtcl2 = xtclMapper.getClsValue("age_type", "2");
        String age1 = xtcl2.getBz_value();                                         //25
        String info1 = xtcl2.getPs();                                              //25岁以上

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
     * 上分查询所有小段位
     */
    @Override
    public ResponseResult getAllItemCat(Integer categoryId) {
        Map<String, Object> map = new HashMap<>();
        List<ItemCat> list = itemCatMapper.getItemCatBy(categoryId, null, 1);
        List<Xtcl> list2 = xtclMapper.getClsBy("gameArea_type", null);
        List<Xtcl> xtclList = xtclMapper.getClsBy("game_type", null);
        List<Xtcl> list1 = xtclMapper.getClsBy("discount", "1");
        Xtcl xtcl = list1.get(0);
        map.put("cats",list);
        map.put("areas",list2);
        map.put("types",xtclList);
        map.put("value",xtcl.getBz_value());
        return ResponseResult.ok(map);
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


    /**
     * 取得标志对应常量值
     * @param bz_type
     * @param bz_id
     * @return
     */
    @Override
    public List<Xtcl> getClsBy(String bz_type, String bz_id) {
        return xtclMapper.getClsBy(bz_type,bz_id);
    }


    /**
     * 根据标识符取得标志对应常量值
     * @param bz_type
     * @param bz_id
     * @return
     */
    @Override
    public Xtcl getClsValue(String bz_type, String bz_id) {
        return xtclMapper.getClsValue(bz_type,bz_id);
    }


    /**
     * 举报
     * @param userReport
     * @return
     */
    @Override
    public int insertUserReport(UserReport userReport) {
        return userReportMapper.insert(userReport);
    }


    /**
     * 用户协议
     * @param key
     * @return
     */
    @Override
    public List<BaseConfig> selectBaseConfig(String key) {
        return baseConfigMapper.selectBaseConfig(key);
    }


    /**
     * 安卓版本更新
     * @param type
     * @return
     */
    @Override
    public List<Xtgx> selectXtgx(Integer type) {
        return baseConfigMapper.selectXtgx(type);
    }


    /**
     * 查询分享
     * @return
     */
    @Override
    public List<BaseShare> getBaseShare(String shareKey) {
        return baseConfigMapper.getBaseShare(shareKey);
    }


}
