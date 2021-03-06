package com.jyss.bacon.action;

import com.jyss.bacon.entity.*;
import com.jyss.bacon.service.ItemService;
import com.jyss.bacon.service.MobileLoginService;
import com.jyss.bacon.service.UserAuthService;
import com.jyss.bacon.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/userInfo")
public class UserInfoAction {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private MobileLoginService mobileLoginService;
    @Autowired
    private UserAuthService userAuthService;


    /**
     * 用户名或id搜索
     */
    @RequestMapping("/search")
    @ResponseBody
    public ResponseResult getUserByNickOrAccount(@RequestParam("account") String account,
                                                 @RequestParam(value = "page", required = true) Integer page,
                                                 @RequestParam(value = "pageSize", required = true) Integer pageSize){
        Page<UserInfo> result = userInfoService.getUserByNickOrAccount(account, page, pageSize);
        return ResponseResult.ok(result);
    }

    /**
     * 根据类目查询
     */
    @RequestMapping("/byCategoryId")
    @ResponseBody
    public ResponseResult getUserByCategoryId(@RequestParam(value = "categoryId") Integer categoryId,
                                              @RequestParam(value = "page", required = true) Integer page,
                                              @RequestParam(value = "pageSize", required = true) Integer pageSize){
        Page<UserInfo> result = userInfoService.getUserByCategoryId(categoryId, page, pageSize);
        return ResponseResult.ok(result);
    }

    /**
     * 条件筛选查询    type: 10 = 全部，11 = 22岁以下，12 = 22-25岁，13 = 25岁以上
     */
    @RequestMapping("/searchBy")
    @ResponseBody
    public ResponseResult getUserInfoBy(@RequestParam("categoryId") Integer categoryId,@RequestParam("ids") String ids,
                                        @RequestParam(value = "page", required = true) Integer page,
                                        @RequestParam(value = "pageSize", required = true) Integer pageSize){
        if(StringUtils.isEmpty(ids)){
            Page<UserInfo> result = userInfoService.getUserByCategoryId(categoryId, page, pageSize);
            return ResponseResult.ok(result);
        }

        String[] id = ids.split(",");
        if(id.length == 3){
            String id1 = id[0];
            String id2 = id[1];
            Integer sex = Integer.valueOf(id1);
            if(sex == 0){
                sex = null;
            }
            if(id2.equals("全部")){
                id2 = null;
            }

            Page<UserInfo> result = userInfoService.getUserInfoBy(categoryId, sex, id2, id[2], page, pageSize);
            return ResponseResult.ok(result);
        }
        return ResponseResult.error("-1","筛选条件无效！");
    }

    /**
     * 查询详细信息
     */
    @RequestMapping("/getUserInfo")
    @ResponseBody
    public ResponseResult findUserDetailInfo(@RequestParam("token") String token,@RequestParam("playId") Integer playId){
        if(StringUtils.isEmpty(token)){
            UserDetailResult result = userInfoService.findUserDetailInfo(null, playId);
            return ResponseResult.ok(result);
        }
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            UserDetailResult result = userInfoService.findUserDetailInfo(uId, playId);
            return ResponseResult.ok(result);
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 查询详细信息(网易账号查找)
     */
    @RequestMapping("/getInfoBy")
    @ResponseBody
    public ResponseResult findUserDetailInfoBy(@RequestParam("token") String token,@RequestParam("accountWy") String accountWy){
        if(StringUtils.isEmpty(token)){
            UserDetailResult result = userInfoService.findUserDetailInfoBy(null, accountWy);
            return ResponseResult.ok(result);
        }
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            UserDetailResult result = userInfoService.findUserDetailInfoBy(uId, accountWy);
            return ResponseResult.ok(result);
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 游戏详细信息
     */
    @RequestMapping("/getUserAuth")
    @ResponseBody
    public ResponseResult selectUserAuth(@RequestParam("authId") Integer authId,@RequestParam("token") String token,
                                         @RequestParam(value = "page", required = true) Integer page,
                                         @RequestParam(value = "pageSize", required = true) Integer pageSize){
        if(StringUtils.isEmpty(token)){
            ResponseResult result = userAuthService.selectUserAuth(null,authId,page,pageSize);
            return ResponseResult.ok(result);
        }
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = userAuthService.selectUserAuth(uId,authId,page,pageSize);
            return result;
        }
        return ResponseResult.error("1","token失效！");

    }




}
