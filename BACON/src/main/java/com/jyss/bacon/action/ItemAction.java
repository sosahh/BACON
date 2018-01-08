package com.jyss.bacon.action;

import com.jyss.bacon.entity.Item;
import com.jyss.bacon.entity.ResponseResult;
import com.jyss.bacon.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return ResponseResult.error("-1","查询失败！");
    }






}
