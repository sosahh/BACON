package com.jyss.bacon.action;

import com.jyss.bacon.entity.*;
import com.jyss.bacon.service.MobileLoginService;
import com.jyss.bacon.service.UserFellowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/fellow")
public class UserFellowAction {

    @Autowired
    private UserFellowService userFellowService;
    @Autowired
    private MobileLoginService mobileLoginService;

    /**
     * 点击关注
     */
    @RequestMapping("/attention")
    @ResponseBody
    public ResponseResult insertUserFellow(@RequestParam("token")String token,@RequestParam("playId")Integer playId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            if(uId == playId){
                return ResponseResult.error("-2","自己无法关注自己！");
            }
            int count = userFellowService.insertUserFellow(uId, playId);
            if (count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","关注失败！");
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 取消关注
     */
    @RequestMapping("/cancel")
    @ResponseBody
    public ResponseResult deleteUserFellow(@RequestParam("token")String token,@RequestParam("playId")Integer playId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            int count = userFellowService.deleteUserFellow(uId, playId);
            if (count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","取消关注失败！");
        }
        return ResponseResult.error("1","token失效！");
    }

    
    /**
     * 查询我的关注        status: 0 = 已关注,1 = 互相关注
     */
    @RequestMapping("/myFellow")
    @ResponseBody
    public ResponseResult getUserFellowById(@RequestParam("token")String token,
                                            @RequestParam(value = "page", required = true) Integer page,
                                            @RequestParam(value = "pageSize", required = true) Integer pageSize){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            Page<UserInfo> result = userFellowService.getUserFellowById(uId, page, pageSize);
            return ResponseResult.ok(result);
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 查询关注我的       status: 0 = 已关注,1 = 互相关注
     */
    @RequestMapping("/fellowMe")
    @ResponseBody
    public ResponseResult getUserFellowByFellowId(@RequestParam("token")String token,
                                                  @RequestParam(value = "page", required = true) Integer page,
                                                  @RequestParam(value = "pageSize", required = true) Integer pageSize){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            Page<UserInfo> result = userFellowService.getUserFellowByFellowId(uId, page, pageSize);
            return ResponseResult.ok(result);
        }
        return ResponseResult.error("1","token失效！");
    }
    


}
