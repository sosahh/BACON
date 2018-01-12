package com.jyss.bacon.action;

import com.jyss.bacon.entity.MobileLogin;
import com.jyss.bacon.entity.Page;
import com.jyss.bacon.entity.ResponseResult;
import com.jyss.bacon.entity.UserDynamic;
import com.jyss.bacon.service.MobileLoginService;
import com.jyss.bacon.service.UserDynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/dynamic")
public class UserDynamicAction {

    @Autowired
    private UserDynamicService userDynamicService;
    @Autowired
    private MobileLoginService mobileLoginService;

    /**
     * 点赞
     */
    @RequestMapping("/clickZan")
    @ResponseBody
    public ResponseResult insertUserPraise(@RequestParam("token")String token, @RequestParam("dynamicId")Integer dynamicId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            int count = userDynamicService.insertUserPraise(uId, dynamicId);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","点赞失败！");
        }
        return ResponseResult.error("1","token失效！");
    }

    /**
     * 取消点赞
     */
    @RequestMapping("/celZan")
    @ResponseBody
    public ResponseResult deletePraiseBy(@RequestParam("token")String token, @RequestParam("dynamicId")Integer dynamicId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            int count = userDynamicService.deletePraiseBy(uId, dynamicId);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","取消点赞失败！");
        }
        return ResponseResult.error("1","token失效！");
    }

    /**
     * 最新动态
     */
    @RequestMapping("/allList")
    @ResponseBody
    public ResponseResult selectAllUserDynamic(@RequestParam(value = "page", required = true) Integer page,
                                               @RequestParam(value = "pageSize", required = true) Integer pageSize){
        Page<UserDynamic> result = userDynamicService.selectUserDynamicBy(null, null, page, pageSize);
        return ResponseResult.ok(result);
    }

    /**
     * 根据性别查询动态    sex：1=女，2=男
     */
    @RequestMapping("/dynamicBySex")
    @ResponseBody
    public ResponseResult selectDynamicBySex(@RequestParam("sex")Integer sex,
                                             @RequestParam(value = "page", required = true) Integer page,
                                             @RequestParam(value = "pageSize", required = true) Integer pageSize){
        Page<UserDynamic> result = userDynamicService.selectUserDynamicBy(null, sex, page, pageSize);
        return ResponseResult.ok(result);
    }

    /**
     * 查询关注人的动态
     */
    @RequestMapping("/fellowDynamic")
    @ResponseBody
    public ResponseResult selectFellowDynamic(@RequestParam("token")String token,
                                              @RequestParam(value = "page", required = true) Integer page,
                                              @RequestParam(value = "pageSize", required = true) Integer pageSize){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            Page<UserDynamic> result = userDynamicService.getDynamicByFellowId(uId, page, pageSize);
            return ResponseResult.ok(result);
        }
        return ResponseResult.error("1","token失效！");
    }

    /**
     * 查询我的动态
     */
    @RequestMapping("/myDynamic")
    @ResponseBody
    public ResponseResult selectMyDynamic(@RequestParam("token")String token,
                                          @RequestParam(value = "page", required = true) Integer page,
                                          @RequestParam(value = "pageSize", required = true) Integer pageSize){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            Page<UserDynamic> result = userDynamicService.selectUserDynamicBy(uId, null, page, pageSize);
            return ResponseResult.ok(result);
        }
        return ResponseResult.error("1","token失效！");
    }

    /**
     * 删除我的动态
     */
    @RequestMapping("/delMyDynamic")
    @ResponseBody
    public ResponseResult deleteUserDynamicById(@RequestParam("token")String token, @RequestParam("dynamicId")Integer dynamicId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            int count = userDynamicService.deleteUserDynamicById(dynamicId);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","删除失败！");
        }
        return ResponseResult.error("1","token失效！");
    }


}
