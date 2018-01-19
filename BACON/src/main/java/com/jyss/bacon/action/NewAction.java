package com.jyss.bacon.action;

import com.jyss.bacon.entity.*;
import com.jyss.bacon.service.ItemService;
import com.jyss.bacon.service.MobileLoginService;
import com.jyss.bacon.service.UserDynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;


@RequestMapping("/news")
@Controller
public class NewAction {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserDynamicService userDynamicService;
    @Autowired
    private MobileLoginService mobileLoginService;


    /**
     * 查看新闻
     */
    @RequestMapping("/newById")
    @ResponseBody
    public ResponseResult getBaseNewById(@RequestParam(value = "token") String token,
                                         @RequestParam(value = "newId") Integer newId){
        if(StringUtils.isEmpty(token)){
            ResponseResult result = itemService.selectBaseNewBy(newId, null);
            return result;
        }

        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = itemService.selectBaseNewBy(newId, uId);
            return result;
        }
        return ResponseResult.error("1","token失效！");

    }



    /**
     * 新闻点赞
     */
    @RequestMapping("/clickZan")
    @ResponseBody
    public ResponseResult insertUserPraise(@RequestParam("token")String token, @RequestParam("newId")Integer newId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            int count = userDynamicService.insertUserPraiseNew(uId, newId);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","点赞失败！");
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 取消新闻点赞
     */
    @RequestMapping("/celZan")
    @ResponseBody
    public ResponseResult deletePraiseBy(@RequestParam("token")String token, @RequestParam("newId")Integer newId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            int count = userDynamicService.deleteNewPraiseBy(uId, newId);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","取消点赞失败！");
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 新闻评价
     */
    @RequestMapping("/evaluate")
    @ResponseBody
    public ResponseResult insertUserComment(@RequestParam("token")String token,@RequestParam("newId")Integer newId,
                                            @RequestParam("content")String content){
        if(content == null || content.trim().length()==0){
            return ResponseResult.error("-2","内容不能为空！");
        }
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = userDynamicService.insertUserCommentNew(uId, newId, content);
            return result;
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 新闻评价删除
     */
    @RequestMapping("/deleteComment")
    @ResponseBody
    public ResponseResult deleteCommentBy(@RequestParam("token")String token,@RequestParam("newId")Integer newId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = userDynamicService.deleteNewCommentBy(newId, uId);
            return result;
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 新闻评价查询
     */
    @RequestMapping("/getComment")
    @ResponseBody
    public ResponseResult selectCommentBy(@RequestParam("newId")Integer newId,
                                          @RequestParam(value = "page", required = true) Integer page,
                                          @RequestParam(value = "pageSize", required = true) Integer pageSize){
        Page<UserComment> result = userDynamicService.selectNewCommentBy(newId, page, pageSize);
        return ResponseResult.ok(result);
    }



}
