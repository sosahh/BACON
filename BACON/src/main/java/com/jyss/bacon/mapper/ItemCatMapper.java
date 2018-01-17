package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.ItemCat;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  ItemCatMapper {

    int insert(ItemCat itemCat);

    int updateByPrimaryKeySelective(ItemCat itemCat);

    int updateByPrimaryKey(ItemCat itemCat);

    //条件查询
    List<ItemCat> getItemCatBy(@Param("categoryId")Integer categoryId,@Param("dwName")String dwName,@Param("status")Integer status);

    //查询所有大段位
    List<ItemCat> selectDwNameByCategoryId(@Param("categoryId")Integer categoryId);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    ItemCat getItemCatById(@Param("id")Integer id);

}