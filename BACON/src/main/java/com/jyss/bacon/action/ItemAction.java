package com.jyss.bacon.action;

import com.jyss.bacon.entity.*;
import com.jyss.bacon.service.ItemService;
import com.jyss.bacon.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/item")
public class ItemAction {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserInfoService userInfoService;


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


    /**
     * 首页展示
     */
    @RequestMapping("/homepage")
    @ResponseBody
    public ResponseResult showHomepage(){
        Map<String, Object> map = new LinkedHashMap<>();
        //标题
        List<BaseNew> news = itemService.getAllNews();
        //类目
        List<Item> items = itemService.selectItem();
        //明星
        Page<UserInfo> star = userInfoService.getStarUserInfo(1, 4);
        //热门
        Page<UserInfo> hot = userInfoService.getHotUserInfo(1, 4);
        //新人
        Page<UserInfo> newUser = userInfoService.getNewUserInfo(1, 4);

        map.put("news",news);
        map.put("items",items);
        map.put("star",star);
        map.put("hot",hot);
        map.put("new",newUser);
        return ResponseResult.ok(map);
    }

    /**
     * 更多明星用户
     */
    @RequestMapping("/star")
    @ResponseBody
    public ResponseResult showStarUserInfo(@RequestParam(value = "page", required = true) Integer page,
                                           @RequestParam(value = "pageSize", required = true) Integer pageSize){
        Page<UserInfo> result = userInfoService.getStarUserInfo(page, pageSize);
        return ResponseResult.ok(result);
    }

    /**
     * 更多热门用户
     */
    @RequestMapping("/hot")
    @ResponseBody
    public ResponseResult showHotUserInfo(@RequestParam(value = "page", required = true) Integer page,
                                           @RequestParam(value = "pageSize", required = true) Integer pageSize){
        Page<UserInfo> result = userInfoService.getHotUserInfo(page, pageSize);
        return ResponseResult.ok(result);
    }

    /**
     * 更多新人用户
     */
    @RequestMapping("/newUser")
    @ResponseBody
    public ResponseResult showNewUserInfo(@RequestParam(value = "page", required = true) Integer page,
                                          @RequestParam(value = "pageSize", required = true) Integer pageSize){
        Page<UserInfo> result = userInfoService.getNewUserInfo(page, pageSize);
        return ResponseResult.ok(result);
    }

    /**
     * 查看新闻
     */
    @RequestMapping("/news")
    @ResponseBody
    public ResponseResult getBaseNewById(@RequestParam(value = "newId") Integer newId){
        List<BaseNew> list = itemService.selectBaseNewBy(newId);
        if(list != null && list.size()>0){
            BaseNew baseNew = list.get(0);
            return ResponseResult.ok(baseNew);
        }
        return ResponseResult.error("-1","查询无结果！");
    }


    /**
     * 条件筛选
     */
    @RequestMapping("/condition")
    @ResponseBody
    public ResponseResult getCondition(@RequestParam("categoryId") Integer categoryId){
        ResponseResult result = itemService.getCondition(categoryId);
        return result;
    }

    /**
     * 查询所有小段位
     */
    @RequestMapping("/xdwList")
    @ResponseBody
    public ResponseResult getItemCatBy(@RequestParam("categoryId") Integer categoryId,@RequestParam("dwName") String dwName){
        List<ItemCat> catList = itemService.getItemCatBy(categoryId, dwName, 1);
        if(catList != null && catList.size()>0){
            return ResponseResult.ok(catList);
        }
        return ResponseResult.error("-1","查询失败！");
    }


}
