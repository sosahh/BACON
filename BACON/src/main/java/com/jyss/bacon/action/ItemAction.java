package com.jyss.bacon.action;

import com.jyss.bacon.entity.*;
import com.jyss.bacon.service.ItemService;
import com.jyss.bacon.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
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
            List<Xtcl> xtclList = itemService.getClsBy("discount", "1");
            if(xtclList != null && xtclList.size()>0){
                Xtcl xtcl = xtclList.get(0);
                String value = xtcl.getBz_value();
                Map<Object, Object> map = new HashMap<>();
                map.put("items",list);
                map.put("value",value);
                return ResponseResult.ok(map);
            }
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
        map.put("newUser",newUser);
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
    public ResponseResult getItemCatBy(@RequestParam("categoryId") Integer categoryId){
        ResponseResult result = itemService.getAllItemCat(categoryId);
        return result;
    }


    /**
     * 查询所有举报项
     */
    @RequestMapping("/reportList")
    @ResponseBody
    public ResponseResult getAllReportList(){
        List<Xtcl> xtclList = itemService.getClsBy("report_type", null);
        return ResponseResult.ok(xtclList);
    }


    /**
     * 用户协议      agType   1=注册协议，2=充值协议，3=提现说明，4=联系方式
     */
    @RequestMapping("/agreement")
    @ResponseBody
    public ResponseResult selectBaseConfig(@RequestParam("agType") Integer agType){
        if(agType == 1){
            List<BaseConfig> configList = itemService.selectBaseConfig("sign.info");
            if(configList != null && configList.size()==1){
                BaseConfig baseConfig = configList.get(0);
                return ResponseResult.ok(baseConfig);
            }

        }else if(agType == 2){
            List<BaseConfig> configList = itemService.selectBaseConfig("czxy.info");
            if(configList != null && configList.size()==1){
                BaseConfig baseConfig = configList.get(0);
                return ResponseResult.ok(baseConfig);
            }
        }else if(agType == 3){
            List<BaseConfig> configList = itemService.selectBaseConfig("take.info");
            if(configList != null && configList.size()==1){
                BaseConfig baseConfig = configList.get(0);
                return ResponseResult.ok(baseConfig);
            }
        }else if(agType == 4){
            List<BaseConfig> configList = itemService.selectBaseConfig("tel.info");
            return ResponseResult.ok(configList);
        }
        return ResponseResult.error("-1","查询失败！");
    }


    /**
     * 安卓版本更新
     */
    @RequestMapping("/version")
    @ResponseBody
    public ResponseResult selectXtgx(){
        List<Xtgx> xtgxList = itemService.selectXtgx(1);      //1=android 2=ios
        if(xtgxList != null && xtgxList.size()==1){
            Xtgx xtgx = xtgxList.get(0);
            return ResponseResult.ok(xtgx);
        }
        return ResponseResult.error("-1","查询失败！");
    }


    /**
     * 帮助与反馈
     */
    @RequestMapping("/help")
    @ResponseBody
    public ResponseResult selectBaseConfigBy(){
        List<BaseConfig> list = itemService.selectBaseConfig("help.info");
        return ResponseResult.ok(list);

    }

    /**
     * 分享
     */
    @RequestMapping("/share")
    @ResponseBody
    public ResponseResult getBaseConfigBy(){

        List<BaseShare> shares = itemService.getBaseShare("app.download");
        if(shares != null && shares.size()==1){
            BaseShare baseShare = shares.get(0);
            return ResponseResult.ok(baseShare);
        }
        return ResponseResult.error("-1","查询失败！");

    }


}
