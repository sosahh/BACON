package com.jyss.bacon.action;

import com.jyss.bacon.constant.Constant;
import com.jyss.bacon.entity.*;
import com.jyss.bacon.filter.MySessionContext;
import com.jyss.bacon.service.*;
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
public class UserSfAction {

    @Autowired
    private UserService userService;
    @Autowired
    private MobileLoginService mobileLoginService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private OrderService orderService;

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




    //////////////代练端APP===上分人员===接口////////////////

    /**
     * 用户登陆
     */
    @RequestMapping("/sf/login")
    @ResponseBody
    public ResponseResult userSfLogin(@RequestParam("tel") String tel,@RequestParam("password") String password){
        ResponseResult result = userService.getUserSf(tel, password);
        return result;
    }

    /**
     * 用户退出
     */
    @RequestMapping("/sf/logout")
    @ResponseBody
    public ResponseResult userSfLogout(@RequestParam("token") String token){
        List<MobileLogin> loginList = mobileLoginService.findUserByTokenBySf(token);
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
            int count = mobileLoginService.insertSfLogin(login);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","退出失败！");
        }
        return ResponseResult.error("1","token失效！");
    }

    /**
     * 发送验证码=======短信===忘记密码===修改密码
     */
    @RequestMapping("/sf/sendCode")
    @ResponseBody
    public ResponseResult sfSendCode(@RequestParam("tel") String tel, HttpServletRequest request) {
        if(tel != null && !"".equals(tel)) {
            //短信修改密码
            List<UserSf> userList = userService.selectUserSfBy(null, tel, "1");
            if (userList != null && userList.size() == 1) {
                Map<String, String> map2 = sendCode(tel, request);
                if (map2.get("msgDo").equals("1")) {
                    return ResponseResult.ok(map2);
                }
                return ResponseResult.error("-1", "操作失败！");
            }
            return ResponseResult.error("-2", "用户不存在！");
        }
        return ResponseResult.error("-3","手机号不能为空！");

    }
    /**
     * 短信修改密码
     */
    @RequestMapping("/sf/updatePwd")
    @ResponseBody
    public ResponseResult sfUpdatePwd(@RequestParam("tel") String tel,@RequestParam("newPwd") String newPwd,
                                    @RequestParam("code") String code,@RequestParam("sessionId") String sessionId){
        if(StringUtils.isEmpty(sessionId)){
            return ResponseResult.error("-1","请重新获取验证码！");
        }
        if(StringUtils.isEmpty(newPwd)){
            return ResponseResult.error("-2","新密码不能为空！");
        }
        if (newPwd.length()<6){
            return ResponseResult.error("-5","密码至少6位！");
        }
        HttpSession session = MySessionContext.getSession(sessionId);
        if (session==null){
            return ResponseResult.error("-1","请重新获取验证码！");
        }
        String checkTel = (String) session.getAttribute("tel");
        String checkCode = (String) session.getAttribute("code");
        if(tel.equals(checkTel) && code.equals(checkCode)){
            //String salt = CommTool.getSalt();
            String pwd = PasswordUtil.generatePayPwd(newPwd);
            List<UserSf> userList = userService.selectUserSfBy(null, tel, "1");
            if(userList != null && userList.size()==1){
                UserSf user = new UserSf();
                user.setPassword(pwd);
                user.setZfPassword(null);
                user.setZfAccount(null);
                user.setZfName(null);
                user.setId(userList.get(0).getId());
                int count = userService.upUserSf(user);
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
    @RequestMapping("/sf/upPassword")
    @ResponseBody
    public ResponseResult sfUpdatePassword(@RequestParam("password") String password,@RequestParam("newPwd") String newPwd,
                                         @RequestParam("token") String token){
        if (newPwd.length()<6){
            return ResponseResult.error("-5","密码至少6位！");
        }
        List<MobileLogin> loginList = mobileLoginService.findUserByTokenBySf(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<UserSf> userList = userService.selectUserSfBy(uId.toString(),null,null);
            if(userList != null && userList.size()==1){
                UserSf user = userList.get(0);
                if(PasswordUtil.generatePayPwd(password).equals(user.getPassword())){
                    //String salt = CommTool.getSalt();
                    String pwd = PasswordUtil.generatePayPwd(newPwd);
                    UserSf user1 = new UserSf();
                    user1.setId(uId);
                    user1.setPassword(pwd);
                    user1.setZfPassword(null);
                    user1.setZfAccount(null);
                    user1.setZfName(null);
                    int count = userService.upUserSf(user1);
                    if(count == 1){
                        return ResponseResult.ok("");
                    }
                    return ResponseResult.error("-4","修改失败！");
                }
                return ResponseResult.error("-1","原密码不正确！");
            }
            return ResponseResult.error("-2","用户信息错误！");
        }
        return ResponseResult.error("1","token失效！");

    }

    /**
     * 设置初始支付密码
     */
    @RequestMapping("/sf/setPayPwd")
    @ResponseBody
    public ResponseResult sfUpdateUserPayPwd(@RequestParam("token")String token,@RequestParam("payPwd")String payPwd){
        if(payPwd == null || payPwd.length() < 6){
            return ResponseResult.error("-2","支付密码需为6位！");
        }
        List<MobileLogin> loginList = mobileLoginService.findUserByTokenBySf(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<UserSf> userList = userService.selectUserSfBy(uId.toString(),null,"1");
            if(userList != null && userList.size()==1){
                UserSf us = userList.get(0);
                //////判断支付密码是否为空，只有为空的时候才能设置初始密码，只有一次机会
                if (us.getZfPassword()==null||us.getZfPassword().equals("")){

                }else{
                    return ResponseResult.error("-3","已设置初始支付密码！");
                }
            }else{
                return ResponseResult.error("-2","用户信息错误！");
            }
            //md5加密
            String newPayPwd = PasswordUtil.generatePayPwd(payPwd);
            UserSf user = new UserSf();
            user.setId(uId);
            user.setZfPassword(newPayPwd);
            user.setZfAccount(null);
            user.setZfName(null);
            user.setPassword(null);
            int count = userService.upUserSf(user);
            if(count == 1){
                return ResponseResult.ok("");
            }
            return ResponseResult.error("-1","支付密码设置失败！");
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 短信修改支付密码
     */
    @RequestMapping("/sf/updatePayPwd")
    @ResponseBody
    public ResponseResult sfUpdatePayPwd(@RequestParam("tel") String tel,@RequestParam("newPwd") String newPwd,
                                      @RequestParam("code") String code,@RequestParam("sessionId") String sessionId){
        if(StringUtils.isEmpty(sessionId)){
            return ResponseResult.error("-1","请重新获取验证码！");
        }
        if(StringUtils.isEmpty(newPwd)){
            return ResponseResult.error("-2","新密码不能为空！");
        }
        if (newPwd.length()<6){
            return ResponseResult.error("-5","密码至少6位！");
        }
        HttpSession session = MySessionContext.getSession(sessionId);
        if (session==null){
            return ResponseResult.error("-1","请重新获取验证码！");
        }
        String checkTel = (String) session.getAttribute("tel");
        String checkCode = (String) session.getAttribute("code");
        if(tel.equals(checkTel) && code.equals(checkCode)){
            //String salt = CommTool.getSalt();
            String pwd = PasswordUtil.generatePayPwd(newPwd);
            List<UserSf> userList = userService.selectUserSfBy(null, tel, "1");
            if(userList != null && userList.size()==1){
                UserSf user = new UserSf();
                user.setZfPassword(pwd);
                user.setZfAccount(null);
                user.setZfName(null);
                user.setPassword(null);
                user.setId(userList.get(0).getId());
                int count = userService.upUserSf(user);
                if(count == 1){
                    return ResponseResult.ok("");
                }
                return ResponseResult.error("-4","修改失败！");
            }
        }
        return ResponseResult.error("-3","验证码不正确！");
    }


    /**
     * 密码修改支付密码
     */
    @RequestMapping("/sf/upPayPassword")
    @ResponseBody
    public ResponseResult sfUpdatePayPassword(@RequestParam("password") String password,@RequestParam("newPwd") String newPwd,
                                           @RequestParam("token") String token){
        if (newPwd.length()<6){
            return ResponseResult.error("-5","密码至少6位！");
        }
        List<MobileLogin> loginList = mobileLoginService.findUserByTokenBySf(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<UserSf> userList = userService.selectUserSfBy(uId.toString(),null,"1");
            if(userList != null && userList.size()==1){
                UserSf user = userList.get(0);
                if(PasswordUtil.generatePayPwd(password).equals(user.getZfPassword())){
                    //String salt = CommTool.getSalt();
                    String pwd = PasswordUtil.generatePayPwd(newPwd);
                    UserSf user1 = new UserSf();
                    user1.setId(uId);
                    user1.setZfPassword(pwd);
                    user1.setZfAccount(null);
                    user1.setZfName(null);
                    user1.setPassword(null);
                    int count = userService.upUserSf(user1);
                    if(count == 1){
                        return ResponseResult.ok("");
                    }
                    return ResponseResult.error("-4","修改失败！");
                }
                return ResponseResult.error("-1","原密码不正确！");
            }
            return ResponseResult.error("-1","原密码不正确！");
        }
        return ResponseResult.error("-2","用户信息错误！");

    }


    /**
     * 添加支付宝账号
     */
    @RequestMapping("/sf/setMyZfAccount")
    @ResponseBody
    public ResponseResult setMyZfAccount(@RequestParam("zfAccount") String zfAccount,@RequestParam("zfName") String zfName,
                                              @RequestParam("token") String token){

        List<MobileLogin> loginList = mobileLoginService.findUserByTokenBySf(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<UserSf> userList = userService.selectUserSfBy(uId.toString(),null,"1");
            if(userList != null && userList.size()==1){
                    UserSf user1 = new UserSf();
                    user1.setId(uId);
                    user1.setZfAccount(zfAccount);
                    user1.setZfName(zfName);
                    user1.setZfPassword(null);
                    user1.setPassword(null);
                    int count = userService.upUserSf(user1);
                    if(count == 1){
                        return ResponseResult.ok("");
                    }
                    return ResponseResult.error("-4","修改失败！");
            }
            return ResponseResult.error("-2","用户信息错误！");
        }
        return ResponseResult.error("1","token失效！");

    }

    /**
     * 删除支付宝账号
     */
    @RequestMapping("/sf/delMyZfAccount")
    @ResponseBody
    public ResponseResult delMyZfAccount(@RequestParam("token") String token){

        List<MobileLogin> loginList = mobileLoginService.findUserByTokenBySf(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<UserSf> userList = userService.selectUserSfBy(uId.toString(),null,"1");
            if(userList != null && userList.size()==1){
                UserSf user1 = userList.get(0);
                if (user1.getZfAccount()==null||user1.getZfAccount().equals("")||user1.getZfName()==null||user1.getZfName().equals("")){
                    return ResponseResult.error("-3","无支付账户！");
                }
                user1.setId(uId);
                user1.setZfAccount("");
                user1.setZfName("");
                user1.setZfPassword(null);
                user1.setPassword(null);
                int count = userService.upUserSf(user1);
                if(count == 1){
                    return ResponseResult.ok("");
                }
                return ResponseResult.error("-4","修改失败！");
            }
            return ResponseResult.error("-2","用户信息错误！");
        }
        return ResponseResult.error("1","token失效！");

    }
    /**
     * 获取支付宝账号ww
     */
    @RequestMapping("/sf/getMyZfAccount")
    @ResponseBody
    public ResponseResult getMyZfAccount(@RequestParam("token") String token){
        Map<String,String > m = new HashMap<String,String>();
        List<MobileLogin> loginList = mobileLoginService.findUserByTokenBySf(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<UserSf> userList = userService.selectUserSfBy(uId.toString(),null,"1");
            if(userList != null && userList.size()==1){
                UserSf user1 = userList.get(0);
                m.put("zfAccount",user1.getZfAccount());
                m.put("zfName",user1.getZfName());
                return ResponseResult.ok(m);
            }
            return ResponseResult.error("-2","用户信息错误！");
        }
        return ResponseResult.error("1","token失效！");

    }


    /**
     * 个人中心
     */
    @RequestMapping("/sf/getMyInfo")
    @ResponseBody
    public Map<String ,Object> getMyInfo(@RequestParam("token") String token){
        Map<String ,Object> m = new HashMap<String ,Object>();
        Map<String ,String> mm = new HashMap<String ,String>();
        m.put("errCode","-1");
        m.put("status","0");
        m.put("errorMsg","token失效！");
        m.put("data","");
        mm.put("zfCode","1");//0=没有支付密码，1=有支付密码
        List<MobileLogin> loginList = mobileLoginService.findUserByTokenBySf(token);
        if (loginList != null && loginList.size() == 1){
            MobileLogin mobileLogin = loginList.get(0);
            Integer uId = mobileLogin.getuId();
            List<UserSf> userList = userService.selectUserSfBy(uId.toString(),null,"1");
            if(userList != null && userList.size()==1){
                UserSf  us = userList.get(0);
                if (us.getZfPassword()==null||us.getZfPassword().equals("")){
                    mm.put("zfCode","0");//0=没有支付密码，1=有支付密码
                }
                mm.put("uname",us.getUname());
                mm.put("balance",us.getBalance()+"");
                ////////累计订单总额
                double orderTotal =0;
                double mytotalBalance = 0;
                ////*status = 0未支付，1已支付，2已接单，3完成，4订单取消 ||reStatus=1 =分配订单 2=完成订单 3=取消订单
                OrderSfView osView = orderService.getSfOrderSumTotal(uId.toString(),"3","2");
                if (osView!=null){
                    orderTotal =osView.getTotal();
                    mytotalBalance =osView.getFinishMoney();
                }
                //////代练订单分成//////
//                String fcPercent = "0.7";
//                Xtcl cl =  itemService.getClsValue("dlfc_type","1");
//                if (cl!=null&&cl.getBz_value()!=null&cl.getBz_value().equals("")){
//                    fcPercent = cl.getBz_value();
//                }
//                double fcBl = Double.parseDouble(fcPercent);
//                double mytotalBalance = fcBl*orderTotal;
                String orderTotal2 = "100";
                m.put("errCode","0");
                m.put("status","1");
                m.put("errorMsg","获取信息成功！");
                mm.put("orderBalance",orderTotal+"");////订单总额
                mm.put("totalBalance",mytotalBalance+"");////共获得的订单总额
                m.put("data",mm);
                return  m;

            }else{
                m.put("errCode","-2");
                m.put("errorMsg","用户信息错误！");
                return m;
            }
        }
        return m;

    }






}
