package com.jyss.bacon.action;

import com.jyss.bacon.entity.Item;
import com.jyss.bacon.entity.ItemCat;
import com.jyss.bacon.entity.ResponseResult;
import com.jyss.bacon.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;


@Controller
@RequestMapping("/item")
public class ItemAction {
    @Autowired
    private ItemService itemService;

    /**
     * 首页标签展示
     */
    @RequestMapping("/category")
    @ResponseBody
    public ResponseResult selectItem(){
        List<Item> list = itemService.selectItem();
        if(list != null && list.size()>0){
            return ResponseResult.ok(list);
        }
        return ResponseResult.error("-1","查询无结果！");
    }

    /**
     * 查询所有大段位
     */
    @RequestMapping("/dwList")
    @ResponseBody
    public ResponseResult selectDwNameByCategoryId(@RequestParam("categoryId") Integer categoryId){
        List<ItemCat> list = itemService.selectDwNameByCategoryId(categoryId);
        if(list != null && list.size()>0){
            return ResponseResult.ok(list);
        }
        return ResponseResult.error("-1","查询无结果！");
    }







}
