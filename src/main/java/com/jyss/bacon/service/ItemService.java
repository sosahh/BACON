package com.jyss.bacon.service;


import com.jyss.bacon.entity.*;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ItemService {

    //查询所有类目
    List<Item> selectItem();

    //查询所有大段位
    List<ItemCat> selectDwNameByCategoryId(@Param("categoryId")Integer categoryId);

    //条件查询段位
    List<ItemCat> getItemCatBy(@Param("categoryId")Integer categoryId,@Param("dwName")String dwName,
                               @Param("status")Integer status);

    //查询所有新闻
    List<BaseNew> getAllNews();

    //条件查询新闻
    ResponseResult selectBaseNewBy(@Param("newId")Integer newId,@Param("uId")Integer uId);

    //条件筛选
    ResponseResult getCondition(@Param("categoryId")Integer categoryId);

    //上分查询所有小段位
    ResponseResult getAllItemCat(@Param("categoryId")Integer categoryId);

    //通过id查询小段位
    ItemCat getItemCatById(@Param("id")Integer id,@Param("categoryId")Integer categoryId);

    //取得标志对应常量值
    List<Xtcl> getClsBy(@Param("bz_type") String bz_type, @Param("bz_id") String bz_id);

    //根据标识符取得标志对应常量值
    Xtcl getClsValue(@Param("bz_type") String bz_type,@Param("bz_id") String bz_id);

    //举报
    int insertUserReport(UserReport userReport);

    //用户协议
    List<BaseConfig> selectBaseConfig(@Param("key")String key);

    //安卓版本更新
    List<Xtgx> selectXtgx(@Param("type")Integer type);

    //查询分享
    List<BaseShare> getBaseShare(@Param("shareKey") String shareKey);
}
