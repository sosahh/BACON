package com.jyss.bacon.action;

import com.jyss.bacon.entity.*;
import com.jyss.bacon.filter.MySessionContext;
import com.jyss.bacon.service.ItemService;
import com.jyss.bacon.service.MobileLoginService;
import com.jyss.bacon.service.UserAuthService;
import com.jyss.bacon.service.UserService;
import com.jyss.bacon.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserAction {

    @Autowired
    private UserService userService;
    @Autowired
    private MobileLoginService mobileLoginService;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private ItemService itemService;


    /**
     * 发送验证码   bz=1 注册，bz=2 短信修改密码，bz=3 添加账户
     */
    @RequestMapping("/sendCode")
    @ResponseBody
    public ResponseResult sendCode(@RequestParam("tel") String tel, @RequestParam("bz") String bz, HttpServletRequest request) {
        if(tel != null && !"".equals(tel)){
            //注册
            if(bz.equals("1")){
                List<User> userList = userService.selectUserBy(null, tel, null);
                if(userList == null || userList.size() == 0){
                    Map<String, String> map2 = sendCode(tel, request);
                    if (map2.get("msgDo").equals("1")) {
                        return ResponseResult.ok(map2);
                    }
                    return ResponseResult.error("-1","操作失败！");
                }
                return ResponseResult.error("-2","该手机号已经注册！");
            }
            //短信修改密码
            if(bz.equals("2")){
                List<User> userList = userService.selectUserBy(null, tel, null);
                if(userList != null && userList.size()==1){
                    Map<String, String> map2 = sendCode(tel, request);
                    if (map2.get("msgDo").equals("1")) {
                        return ResponseResult.ok(map2);
                    }
                    return ResponseResult.error("-1","操作失败！");
                }
                return ResponseResult.error("-2","用户不存在！");
            }
            //支付宝
            if(bz.equals("3")){
                Map<String, String> map2 = sendCode(tel, request);
                if (map2.get("msgDo").equals("1")) {
                    return ResponseResult.ok(map2);
                }
                return ResponseResult.error("-1","操作失败！");
            }
        }
        return ResponseResult.error("-3","手机号不能为空！");

    }

    /**
     * 发送验证码
     */
    private Map<String, String> sendCode(String tel, HttpServletRequest request) {
        Map<String, String> m = new HashMap<String, String>();
        String code = CommTool.getYzm();
        //添加到session
        HttpSession session = request.getSession();
        String sessionId = "";
        sessionId = session.getId();
        session.removeAttribute("code");
        session.setAttribute("code",code);
        session.removeAttribute("tel");
        session.setAttribute("tel",tel);
        //设置过期时间
        session.setMaxInactiveInterval(10 * 60);
        //发送短信
        String msgDo = HttpClientUtil.MsgDo(tel, "您此次操作的验证码为：" + code + "，请尽快在10分钟内完成验证。");
        m.put("sessionId", sessionId);
        m.put("msgDo", msgDo);
        return m;
    }


    /**
     * 短信验证码确认
     */
    @RequestMapping("/validationCode")
    @ResponseBody
    public ResponseResult validationCode(@RequestParam("tel") String tel,@RequestParam("code") String code,
                                         @RequestParam("sessionId") String sessionId){
        if(StringUtils.isEmpty(sessionId)){
            return ResponseResult.error("-1","请重新获取验证码！");
        }
        List<User> userList = userService.selectUserBy(null, tel, null);
        if(userList == null || userList.size() == 0){
            HttpSession session = MySessionContext.getSession(sessionId);
            String checkTel = (String) session.getAttribute("tel");
            String checkCode = (String) session.getAttribute("code");

            if(tel.equals(checkTel) && code.equals(checkCode)){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-3","验证码不正确！");
        }
        return ResponseResult.error("-2","该手机号已经注册！");
    }


    /**
     * 用户注册
     */
    @RequestMapping(value = "/regist",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult insertUser(User user,HttpServletRequest request, HttpServletResponse response)throws Exception{
        if(StringUtils.isEmpty(user.getPassword())){
            return ResponseResult.error("-2","密码不能为空！");
        }
        //user.setAccount();
        String salt = CommTool.getSalt();
        String pwd = PasswordUtil.generate(user.getPassword(), salt);

        user.setPassword(pwd);
        user.setSalt(salt);

        //设置头像
        // Base64.decode(photo);
        String photo = user.getHeadpic();
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");

        String filePath = request.getSession().getServletContext().getRealPath("/");
        int index = filePath.indexOf("BACON");
        filePath = filePath.substring(0, index) + "uploadHeadPic" + "/";
        File f = new File(filePath);
        CommTool.judeDirExists(f);
        filePath = filePath + Utils.getSaltFour() + System.currentTimeMillis()+ ".png";
        boolean isOk = false;
        isOk = Base64Image.GenerateImage(photo, filePath);
        if (isOk) {
            user.setHeadpic(filePath.substring(filePath.indexOf("uploadHeadPic")));
        }

        //设置年龄
        user.setAge(DateFormatUtils.getAge(user.getBirthDate()));
        user.setPlace("上海");           //默认设置
        user.setProfession("学生");      //默认设置
        user.setHobby("玩游戏");         //默认设置
        user.setStatus(1);
        user.setIsAuth(0);
        user.setCreateTime(new Date());
        user.setLastModifyTime(new Date());

        ResponseResult result = userService.insert(user);
        return result;
    }

    /**
     * 用户登陆
     */
    @RequestMapping("/login")
    @ResponseBody
    public ResponseResult userLogin(@RequestParam("tel") String tel,@RequestParam("password") String password){
        ResponseResult result = userService.getUser(tel, password);
        return result;
    }


    /**
     * 用户第三方登陆
     */
    @RequestMapping("/loginBy")
    @ResponseBody
    public ResponseResult userLoginBy(@RequestParam("openId") String openId,
                                      @RequestParam("unionId") String unionId){
        if(StringUtils.isEmpty(openId) && StringUtils.isEmpty(unionId)){
            return ResponseResult.error("-4","登陆失败！");
        }
        ResponseResult result = userService.getUserByOpenId(openId, unionId);
        return result;
    }


    /**
     * 用户个人资料查询
     */
    @RequestMapping("/userInfo")
    @ResponseBody
    public ResponseResult getUserInfo(@RequestParam("token") String token){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();

            ResponseResult result = userService.getUserInfo(uId + "");
            return result;
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 用户退出
     */
    @RequestMapping("/logout")
    @ResponseBody
    public ResponseResult userLogout(@RequestParam("token") String token){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            //记录登陆信息
            String newToken = CommTool.getUUID();
            MobileLogin login = new MobileLogin();
            login.setuId(uId);
            login.setToken(newToken);
            login.setLastAccessTime(System.currentTimeMillis());
            login.setStatus(1);
            login.setCreatedAt(new Date());
            int count = mobileLoginService.insert(login);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","退出失败！");
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 编辑资料
     */
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult updateUserInfo(User user, @RequestParam("token") String token){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<User> userList = userService.selectUserBy(uId+"", null, null);
            User user1 = userList.get(0);
            user.setuId(uId);
            user.setLastModifyTime(new Date());
            if(!user1.getNick().equals(user.getNick()) && !StringUtils.isEmpty(user.getNick())){
                String status = WangyiyunUtils.updateWangyiyun(user1.getAccountWy(), user.getNick(), null);
                if(!status.equals("200")){
                    return ResponseResult.error("-1","修改失败！");
                }
            }
            int count = userService.updateUser(user);
            if(count == 1){
                return ResponseResult.ok("");
            }

            return ResponseResult.error("-1","修改失败！");
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 修改头像
     */
    @RequestMapping(value = "/upHeadPic",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult updateHeadPic(@RequestParam("token") String token,@RequestParam("headPic") String headPic,
                                         HttpServletRequest request, HttpServletResponse response)throws Exception{
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            if(StringUtils.isEmpty(headPic)){
                return ResponseResult.error("-1","修改失败！");
            }

            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<User> userList = userService.selectUserBy(uId+"", null, null);
            User user1 = userList.get(0);
            User user = new User();
            user.setuId(uId);

            // Base64.decode(photo);
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html");

            String filePath = request.getSession().getServletContext().getRealPath("/");
            int index = filePath.indexOf("BACON");
            filePath = filePath.substring(0, index) + "uploadHeadPic" + "/";
            File f = new File(filePath);
            CommTool.judeDirExists(f);
            filePath = filePath + uId + System.currentTimeMillis() + ".png";
            boolean isOk = false;
            isOk = Base64Image.GenerateImage(headPic, filePath);
            if (isOk) {
                user.setLastModifyTime(new Date());
                user.setHeadpic(filePath.substring(filePath.indexOf("uploadHeadPic")));
                String status = WangyiyunUtils.updateWangyiyun(user1.getAccountWy(), null, filePath);
                if(status.equals("200")){
                    int count = userService.updateUser(user);
                    if(count == 1){
                        return ResponseResult.ok("");
                    }
                }
            }
            return ResponseResult.error("-1","修改失败！");
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 短信修改密码
     */
    @RequestMapping("/updatePwd")
    @ResponseBody
    public ResponseResult updatePwd(@RequestParam("tel") String tel,@RequestParam("newPwd") String newPwd,
                                    @RequestParam("code") String code,@RequestParam("sessionId") String sessionId){
        if(StringUtils.isEmpty(sessionId)){
            return ResponseResult.error("-1","请重新获取验证码！");
        }
        if(StringUtils.isEmpty(newPwd)){
            return ResponseResult.error("-2","新密码不能为空！");
        }
        HttpSession session = MySessionContext.getSession(sessionId);
        String checkTel = (String) session.getAttribute("tel");
        String checkCode = (String) session.getAttribute("code");
        if(tel.equals(checkTel) && code.equals(checkCode)){
            String salt = CommTool.getSalt();
            String pwd = PasswordUtil.generate(newPwd, salt);
            List<User> userList = userService.selectUserBy(null, tel, null);
            if(userList != null && userList.size()==1){
                User user = new User();
                user.setPassword(pwd);
                user.setSalt(salt);
                user.setuId(userList.get(0).getuId());
                int count = userService.updateUser(user);
                if(count == 1){
                    return ResponseResult.ok("");
                }
                return ResponseResult.error("-4","修改失败！");
            }
        }
        return ResponseResult.error("-3","验证码不正确！");
    }


    /**
     * 密码修改密码
     */
    @RequestMapping("/upPassword")
    @ResponseBody
    public ResponseResult updatePassword(@RequestParam("password") String password,@RequestParam("newPwd") String newPwd,
                                    @RequestParam("token") String token){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<User> userList = userService.selectUserBy(uId + "", null, null);
            if(userList != null && userList.size()==1){
                User user = userList.get(0);
                if(PasswordUtil.generate(password, user.getSalt()).equals(user.getPassword())){
                    String salt = CommTool.getSalt();
                    String pwd = PasswordUtil.generate(newPwd, salt);
                    User user1 = new User();
                    user1.setuId(uId);
                    user1.setPassword(pwd);
                    user1.setSalt(salt);
                    int count = userService.updateUser(user1);
                    if(count == 1){
                        return ResponseResult.ok("");
                    }
                    return ResponseResult.error("-4","修改失败！");
                }
                return ResponseResult.error("-1","原密码不正确！");
            }
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 游戏认证
     */
    @RequestMapping("/userAuth")
    @ResponseBody
    public ResponseResult insertUserAuth(UserAuth userAuth,@RequestParam("token") String token,
                                         HttpServletRequest request, HttpServletResponse response)throws Exception{
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            if(StringUtils.isEmpty(userAuth.getTitleId())||StringUtils.isEmpty(userAuth.getCategoryId())){
                return ResponseResult.error("-1","提交失败！");
            }

            List<UserAuth> userAuthList1 = userAuthService.getUserAuthBy(uId, userAuth.getCategoryId(), 1);
            if(userAuthList1 != null && userAuthList1.size()>0){
                return ResponseResult.error("-3","正在审核中，请等待！");
            }

            ItemCat itemCat = itemService.getItemCatById(userAuth.getTitleId());
            userAuth.setuId(uId);
            userAuth.setCategoryTitle(itemCat.getCategoryName());
            userAuth.setTitlePwName(itemCat.getDwName());
            userAuth.setTitleName(itemCat.getName());
            userAuth.setStatus(1);
            userAuth.setIsShelve(1);
            userAuth.setCreated(new Date());

            // Base64.decode(photo);
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html");

            String filePath = request.getSession().getServletContext().getRealPath("/");
            int index = filePath.indexOf("BACON");
            filePath = filePath.substring(0, index) + "uploadAuthPic" + "/";
            File f = new File(filePath);
            CommTool.judeDirExists(f);
            String filePath1 = filePath + uId + System.currentTimeMillis() + "01.png";
            String filePath2 = filePath + uId + System.currentTimeMillis() + "02.png";
            String filePath3 = filePath + uId + System.currentTimeMillis() + "03.png";
            boolean isOk1 = false;
            isOk1 = Base64Image.GenerateImage(userAuth.getPicture1(), filePath1);
            if (isOk1) {
                userAuth.setPicture1(filePath1.substring(filePath1.indexOf("uploadAuthPic")));
            }
            boolean isOk2 = false;
            isOk2 = Base64Image.GenerateImage(userAuth.getPicture2(), filePath2);
            if (isOk2) {
                userAuth.setPicture2(filePath2.substring(filePath2.indexOf("uploadAuthPic")));
            }
            boolean isOk3 = false;
            isOk3 = Base64Image.GenerateImage(userAuth.getPicture3(), filePath3);
            if (isOk3) {
                userAuth.setPicture3(filePath3.substring(filePath3.indexOf("uploadAuthPic")));
            }
            //修改游戏认证
            List<UserAuth> userAuthList = userAuthService.getUserAuthBy(uId, userAuth.getCategoryId(), 2);
            if(userAuthList != null && userAuthList.size()>0){
                UserAuth userAuth1 = userAuthList.get(0);
                userAuth.setId(userAuth1.getId());
                int count = userAuthService.updateByPrimaryKeySelective(userAuth);
                if(count == 1){
                    return ResponseResult.ok("");
                }
                return ResponseResult.error("-2","修改失败！");
            }
            //添加游戏认证
            int count = userAuthService.insert(userAuth);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","提交失败！");
        }
        return ResponseResult.error("1","token失效！");

    }

    /**
     * 设置支付密码
     */
    @RequestMapping("/setPayPwd")
    @ResponseBody
    public ResponseResult updateUserPayPwd(@RequestParam("token")String token,@RequestParam("payPwd")String payPwd){
        if(payPwd == null || payPwd.length() != 6){
            return ResponseResult.error("-2","支付密码需为6位！");
        }
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            //String pwd = PasswordUtil.generatePayPwd(payPwd);             //md5加密
            String newPayPwd = DigestUtils.md5DigestAsHex(payPwd.getBytes());
            User user = new User();
            user.setuId(uId);
            user.setPayPwd(newPayPwd);
            int count = userService.updateUser(user);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","支付密码设置失败！");
        }
        return ResponseResult.error("1","token失效！");

    }

    /**
     * 原密码修改支付密码
     */
    @RequestMapping("/upPayPassword")
    @ResponseBody
    public ResponseResult upPayPwdByPayPwd(@RequestParam("token")String token,
                                           @RequestParam("payPwd")String payPwd,@RequestParam("newPayPwd")String newPayPwd){
        if(newPayPwd == null || newPayPwd.length() != 6){
            return ResponseResult.error("-2","支付密码需为6位！");
        }
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<User> userList = userService.selectUserBy(uId + "", null, null);
            if(userList != null && userList.size()==1){
                User user = userList.get(0);
                if(DigestUtils.md5DigestAsHex(payPwd.getBytes()).equals(user.getPayPwd())){
                    String pwd = DigestUtils.md5DigestAsHex(newPayPwd.getBytes());
                    User user1 = new User();
                    user1.setuId(uId);
                    user1.setPayPwd(pwd);
                    int count = userService.updateUser(user1);
                    if(count == 1){
                        return ResponseResult.ok("");
                    }
                    return ResponseResult.error("-4","修改失败！");
                }
                return ResponseResult.error("-1","原密码不正确！");
            }
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 短信修改支付密码
     */
    @RequestMapping("/updatePayPwd")
    @ResponseBody
    public ResponseResult updatePayPwdBy(@RequestParam("tel") String tel,@RequestParam("newPayPwd") String newPayPwd,
                                    @RequestParam("code") String code,@RequestParam("sessionId") String sessionId){
        if(StringUtils.isEmpty(sessionId)){
            return ResponseResult.error("-1","请重新获取验证码！");
        }
        if(newPayPwd == null || newPayPwd.length() != 6){
            return ResponseResult.error("-2","支付密码需为6位！");
        }
        HttpSession session = MySessionContext.getSession(sessionId);
        String checkTel = (String) session.getAttribute("tel");
        String checkCode = (String) session.getAttribute("code");
        if(tel.equals(checkTel) && code.equals(checkCode)){
            String pwd = DigestUtils.md5DigestAsHex(newPayPwd.getBytes());
            List<User> userList = userService.selectUserBy(null, tel, null);
            if(userList != null && userList.size()==1){
                User user = new User();
                user.setPayPwd(pwd);
                user.setuId(userList.get(0).getuId());
                int count = userService.updateUser(user);
                if(count == 1){
                    return ResponseResult.ok("");
                }
                return ResponseResult.error("-4","修改失败！");
            }
        }
        return ResponseResult.error("-3","验证码不正确！");
    }


    /**
     * 游戏下架和删除      yxType: 1=删除，2=下架
     */
    @RequestMapping("/upUserAuth")
    @ResponseBody
    public ResponseResult updateUserAuth(@RequestParam("token")String token,@RequestParam("authId")Integer authId,
                                         @RequestParam("yxType")Integer yxType){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            UserAuth userAuth = new UserAuth();
            userAuth.setId(authId);
            if(yxType == 1){
                userAuth.setStatus(0);
            }else if(yxType == 2){
                userAuth.setIsShelve(0);
            }
            int count = userAuthService.updateByPrimaryKeySelective(userAuth);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","操作失败！");
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 游戏上架
     */
    @RequestMapping("/upAuthShelve")
    @ResponseBody
    public ResponseResult updateUserAuthShelve(@RequestParam("token")String token,@RequestParam("authId")Integer authId){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            UserAuth userAuth = new UserAuth();
            userAuth.setId(authId);
            userAuth.setIsShelve(1);
            int count = userAuthService.updateByPrimaryKeySelective(userAuth);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","操作失败！");
        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 我的评价
     */
    @RequestMapping("/getMyEvaluate")
    @ResponseBody
    public ResponseResult getMyEvaluate(@RequestParam("token") String token,
                                        @RequestParam(value = "page", required = true) Integer page,
                                        @RequestParam(value = "pageSize", required = true) Integer pageSize){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = userAuthService.getMyEvaluate(uId, page, pageSize);
            return result;

        }
        return ResponseResult.error("1","token失效！");
    }


    /**
     * 举报动态
     */
    @RequestMapping("/report")
    @ResponseBody
    public ResponseResult insertUserReport(@RequestParam("token") String token,UserReport userReport,
                                           @RequestParam("playId") Integer playId){
        if((userReport.getContent() == null || userReport.getContent().trim().length()==0)&&
                StringUtils.isEmpty(userReport.getReportName())){
            return ResponseResult.error("-1","操作失败！");
        }
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<User> userList = userService.selectUserBy(playId+"", null, null);
            User user = userList.get(0);

            userReport.setuId(uId);
            userReport.setAccount(user.getAccount());
            userReport.setStatus(1);
            userReport.setResult(0);
            userReport.setCreateTime(new Date());
            userReport.setModifyTime(new Date());
            int count = itemService.insertUserReport(userReport);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","操作失败！");
        }
        return ResponseResult.error("1","token失效！");

    }

    /**
     * 意见反馈
     */
    @RequestMapping("/feedback")
    @ResponseBody
    public ResponseResult insertUserReport(@RequestParam("token") String token,@RequestParam("content") String content){
        if(content == null || content.trim().length()==0){
            return ResponseResult.error("-1","操作失败！");
        }
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            UserReport userReport = new UserReport();
            userReport.setuId(uId);
            userReport.setContent(content);
            userReport.setStatus(2);
            userReport.setResult(0);
            userReport.setCreateTime(new Date());
            userReport.setModifyTime(new Date());
            int count = itemService.insertUserReport(userReport);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","操作失败！");
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 我的收入
     */
    @RequestMapping("/wallet")
    @ResponseBody
    public ResponseResult selectUserWallet(@RequestParam("token") String token){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = userService.selectUserWallet(uId);
            return result;
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 我的培根币账单
     */
    @RequestMapping("/scoreList")
    @ResponseBody
    public ResponseResult selectScoreBalance(@RequestParam("token") String token,
                                             @RequestParam(value = "page", required = true) Integer page,
                                             @RequestParam(value = "pageSize", required = true) Integer pageSize){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = userService.selectScoreBalance(uId, page, pageSize);
            return result;
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 收入账单
     */
    @RequestMapping("/chargeList")
    @ResponseBody
    public ResponseResult selectMoneyDetail(@RequestParam("token") String token,
                                             @RequestParam(value = "page", required = true) Integer page,
                                             @RequestParam(value = "pageSize", required = true) Integer pageSize){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = userService.selectMoneyDetail(uId, page, pageSize);
            return result;
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 我的培根币
     */
    @RequestMapping("/balance")
    @ResponseBody
    public ResponseResult selectUserBalance(@RequestParam("token") String token){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = userService.selectUserBalance(uId);
            return result;
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 添加我的账户
     */
    @RequestMapping("/account")
    @ResponseBody
    public ResponseResult insertUserAccount(@RequestParam("realName") String realName,@RequestParam("token") String token,
                                            @RequestParam("tel") String tel,@RequestParam("code") String code,
                                            @RequestParam("sessionId") String sessionId){
        if(StringUtils.isEmpty(sessionId)){
            return ResponseResult.error("-1","请重新获取验证码！");
        }

        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<UserAccount> list = userService.getUserAccount(null,tel);
            if(list != null && list.size()>0){
                return ResponseResult.error("-4","账号已存在！");
            }
            HttpSession session = MySessionContext.getSession(sessionId);
            String checkTel = (String) session.getAttribute("tel");
            String checkCode = (String) session.getAttribute("code");
            if(tel.equals(checkTel) && code.equals(checkCode)){
                UserAccount userAccount = new UserAccount();
                userAccount.setuId(uId);
                userAccount.setRealName(realName);
                userAccount.setAccount(tel);
                userAccount.setStatus(1);
                userAccount.setType(1);
                userAccount.setCreateTime(new Date());
                int count = userService.insertUserAccount(userAccount);
                if(count == 1){
                    return ResponseResult.ok("");
                }
                return ResponseResult.error("-2","添加失败！");
            }
            return ResponseResult.error("-3","验证码不正确！");
        }
        return ResponseResult.error("1","token失效！");

    }



    /**
     * 查询我的账户
     */
    @RequestMapping("/getAccount")
    @ResponseBody
    public ResponseResult getUserAccount(@RequestParam("token") String token){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<UserAccount> list = userService.getUserAccount(uId,null);
            return ResponseResult.ok(list);
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 删除账户
     */
    @RequestMapping("/delAccount")
    @ResponseBody
    public ResponseResult getUserAccount(@RequestParam("token") String token,@RequestParam("aIds") String aIds){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            String[] ids = aIds.split(",");
            for (String id : ids) {
                UserAccount userAccount = new UserAccount();
                userAccount.setId(Integer.parseInt(id));
                userAccount.setStatus(0);
                userService.updateUserAccount(userAccount);

            }
            return ResponseResult.ok("");
        }
        return ResponseResult.error("1","token失效！");

    }

    /**
     * 系统消息
     */
    @RequestMapping("/message")
    @ResponseBody
    public ResponseResult getUserReport(@RequestParam("token") String token,
                                        @RequestParam(value = "page", required = true) Integer page,
                                        @RequestParam(value = "pageSize", required = true) Integer pageSize){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = userService.getUserReport(uId, page, pageSize);
            return result;

        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 提现
     */
    @RequestMapping("/withdrawals")
    @ResponseBody
    public ResponseResult insertScoreEarn(@RequestParam("token") String token,@RequestParam("account") String account,
                                          @RequestParam("cash") Float cash,@RequestParam("payPwd") String payPwd,
                                          @RequestParam("realName") String realName){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = userService.insertScoreEarn(uId, account, cash, payPwd, realName);
            return result;

        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 充值          czType: 1=支付宝，2=微信
     */
    @RequestMapping("/recharge")
    @ResponseBody
    public ResponseResult updateUserBalance(@RequestParam("token") String token,@RequestParam("cash") Float cash,
                                     @RequestParam("czType") Integer czType){
        List<MobileLogin> loginList = mobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            ResponseResult result = userService.updateUserBalance(uId, cash, czType);
            return result;

        }
        return ResponseResult.error("1","token失效！");

    }


}
