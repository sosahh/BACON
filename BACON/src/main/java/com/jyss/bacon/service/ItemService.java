package com.jyss.bacon.service;


import com.jyss.bacon.entity.*;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ItemService {

    //查询所有类目
    List<Item> selectItem();

    //查询所有大段位
    List<ItemCat> selectDwNameByCategoryId(@Param("categoryId")Integer categoryId);

    //查询所有新闻
    List<BaseNew> getAllNews();

    //条件查询新闻
    ResponseResult selectBaseNewBy(@Param("newId")Integer newId,@Param("uId")Integer uId);

    //条件筛选
    ResponseResult getCondition(@Param("categoryId")Integer categoryId);

    //查询所有小段位
    ResponseResult getAllItemCat(@Param("categoryId")Integer categoryId);

    //通过id查询小段位
    ItemCat getItemCatById(@Param("id")Integer id);

    //取得标志对应常量值
    List<Xtcl> getClsBy(@Param("bz_type") String bz_type, @Param("bz_id") String bz_id);
}
