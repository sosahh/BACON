package com.jyss.bacon.action;

import com.jyss.bacon.entity.User;
import com.jyss.bacon.service.UserService;
import com.jyss.bacon.utils.CommTool;
import com.jyss.bacon.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserAction {

    @Autowired
    private UserService userService;


    /**
     * 发送验证码   bz=1 注册，bz=2忘记密码
     */
    @RequestMapping("/sendCode")
    @ResponseBody
    public Map<String, Object> sendCode(@RequestParam("tel") String tel,@RequestParam("bz") String bz,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        if(tel != null && !"".equals(tel)){
            //注册
            if(bz.equals("1")){
                List<User> userList = userService.selectUserBy(null, tel, null);
                if(userList == null || userList.size() == 0){
                    Map<String, String> map2 = sendCode(tel, request);
                    if (map2.get("msgDo").equals("1")) {
                        map.put("code", "0");
                        map.put("status", "true");
                        map.put("message", "操作成功！");
                        map.put("data", map2);
                        return map;
                    }
                    map.put("code", "-1");
                    map.put("status", "false");
                    map.put("message", "操作失败！");
                    map.put("data", "");
                    return map;
                }
                map.put("code", "-3");
                map.put("status", "false");
                map.put("message", "该手机号已注册！");
                map.put("data", "");
                return map;
            }
            //忘记密码



        }
        map.put("code", "-2");
        map.put("status", "false");
        map.put("message", "手机号不能为空");
        map.put("data", "");
        return map;

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
     * 用户注册
     */
    /*@RequestMapping("/regist")
    @ResponseBody
    public Map<String,Object> insertUser(User user){

        return null;
    }
*/
}
