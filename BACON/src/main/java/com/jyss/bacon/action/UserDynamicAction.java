package com.jyss.bacon.action;

import com.jyss.bacon.entity.*;
import com.jyss.bacon.service.MobileLoginService;
import com.jyss.bacon.service.UserDynamicService;
import com.jyss.bacon.utils.Base64Image;
import com.jyss.bacon.utils.CommTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
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
    public ResponseResult selectAllUserDynamic(@RequestParam("token")String token,
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
     * 根据性别查询动态    sex：1=女，2=男
     */
    @RequestMapping("/dynamicBySex")
    @ResponseBody
    public ResponseResult selectDynamicBySex(@RequestParam("token")String token,@RequestParam("sex")Integer sex,
                                             @RequestParam(value = "page", required = true) Integer page,
                                             @RequestParam(value = "pageSize", required = true) Integer pageSize){
        /*Page<UserDynamic> result = userDynamicService.selectUserDynamicBy(null, sex, page, pageSize);
        return ResponseResult.ok(result);*/
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            Page<UserDynamic> result = userDynamicService.selectUserDynamicBy(uId, sex, page, pageSize);
            return ResponseResult.ok(result);
        }
        return ResponseResult.error("1","token失效！");
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
            Page<UserDynamic> result = userDynamicService.selectMyUserDynamic(uId, page, pageSize);
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

    /**
     * 发布动态
     */
    @RequestMapping("/releaseDynamic")
    @ResponseBody
    public ResponseResult insertDynamic(UserDynamic userDynamic, @RequestParam("token")String token,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();

            if(StringUtils.isEmpty(userDynamic.getPicture1())&& StringUtils.isEmpty(userDynamic.getContent())){
                return ResponseResult.error("-2","内容不能为空！");
            }

            userDynamic.setuId(uId);
            userDynamic.setStatus(1);
            userDynamic.setCreated(new Date());

            String picture1 = userDynamic.getPicture1();
            String picture2 = userDynamic.getPicture2();
            String picture3 = userDynamic.getPicture3();
            String picture4 = userDynamic.getPicture4();
            String picture5 = userDynamic.getPicture5();
            String picture6 = userDynamic.getPicture6();

            // Base64.decode(photo);
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html");

            String filePath = request.getSession().getServletContext().getRealPath("/");
            int index = filePath.indexOf("BACON");
            filePath = filePath.substring(0, index) + "uploadDyPic" + "/";
            File f = new File(filePath);
            CommTool.judeDirExists(f);

            String filePath1 = filePath + uId + System.currentTimeMillis() + "01.png";
            String filePath2 = filePath + uId + System.currentTimeMillis() + "02.png";
            String filePath3 = filePath + uId + System.currentTimeMillis() + "03.png";
            String filePath4 = filePath + uId + System.currentTimeMillis() + "04.png";
            String filePath5 = filePath + uId + System.currentTimeMillis() + "05.png";
            String filePath6 = filePath + uId + System.currentTimeMillis() + "06.png";
            boolean isOk1 = false;
            isOk1 = Base64Image.GenerateImage(picture1, filePath1);
            if (isOk1) {
                userDynamic.setPicture1(filePath1.substring(filePath1.indexOf("uploadDyPic")));
            }
            boolean isOk2 = false;
            isOk2 = Base64Image.GenerateImage(picture2, filePath2);
            if (isOk2) {
                userDynamic.setPicture2(filePath2.substring(filePath2.indexOf("uploadDyPic")));
            }
            boolean isOk3 = false;
            isOk3 = Base64Image.GenerateImage(picture3, filePath3);
            if (isOk3) {
                userDynamic.setPicture3(filePath3.substring(filePath3.indexOf("uploadDyPic")));
            }
            boolean isOk4 = false;
            isOk4 = Base64Image.GenerateImage(picture4, filePath4);
            if (isOk4) {
                userDynamic.setPicture4(filePath4.substring(filePath4.indexOf("uploadDyPic")));
            }
            boolean isOk5 = false;
            isOk5 = Base64Image.GenerateImage(picture5, filePath5);
            if (isOk5) {
                userDynamic.setPicture5(filePath5.substring(filePath5.indexOf("uploadDyPic")));
            }
            boolean isOk6 = false;
            isOk6 = Base64Image.GenerateImage(picture6, filePath6);
            if (isOk6) {
                userDynamic.setPicture6(filePath6.substring(filePath6.indexOf("uploadDyPic")));
            }

            int count = userDynamicService.insert(userDynamic);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","发布失败！");
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 动态评价
     */
    @RequestMapping("/evaluate")
    @ResponseBody
    public ResponseResult insertUserComment(@RequestParam("token")String token,@RequestParam("dynamicId")Integer dynamicId,
                                            @RequestParam("content")String content){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = userDynamicService.insertUserComment(uId, dynamicId, content);
            return result;
        }
        return ResponseResult.error("1","token失效！");

    }

    /**
     * 动态评价查询
     */
    @RequestMapping("/getComment")
    @ResponseBody
    public ResponseResult selectCommentBy(@RequestParam("dynamicId")Integer dynamicId,
                                          @RequestParam(value = "page", required = true) Integer page,
                                          @RequestParam(value = "pageSize", required = true) Integer pageSize){
        Page<UserComment> result = userDynamicService.selectCommentBy(dynamicId, page, pageSize);
        return ResponseResult.ok(result);
    }


    /**
     * 动态删除
     */
    @RequestMapping("/deleteComment")
    @ResponseBody
    public ResponseResult deleteCommentBy(@RequestParam("token")String token,@RequestParam("dynamicId")Integer dynamicId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = userDynamicService.deleteCommentBy(dynamicId, uId);
            return result;
        }
        return ResponseResult.error("1","token失效！");
    }

}
