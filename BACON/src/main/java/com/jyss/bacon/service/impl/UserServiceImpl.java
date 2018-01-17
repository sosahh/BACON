package com.jyss.bacon.service.impl;

import com.jyss.bacon.entity.MobileLogin;
import com.jyss.bacon.entity.ResponseResult;
import com.jyss.bacon.entity.User;
import com.jyss.bacon.mapper.MobileLoginMapper;
import com.jyss.bacon.mapper.UserMapper;
import com.jyss.bacon.service.UserService;
import com.jyss.bacon.utils.CommTool;
import com.jyss.bacon.utils.PasswordUtil;
import com.jyss.bacon.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MobileLoginMapper mobileLoginMapper;


    /**
     * 用户注册
     */
    @Override
    public ResponseResult insert(User user) {
        int insert = userMapper.insert(user);
        if(insert == 1){
            Integer uId = user.getuId();
            //设置账号
            User user1 = new User();
            user1.setuId(uId);
            user1.setAccount(Utils.getMyId(uId+""));
            userMapper.updateByPrimaryKeySelective(user1);
            //记录登陆信息
            MobileLogin mobileLogin = new MobileLogin();
            String token = CommTool.getUUID();
            mobileLogin.setuId(uId);
            mobileLogin.setToken(token);
            mobileLogin.setLastAccessTime(System.currentTimeMillis());
            mobileLogin.setStatus(1);
            mobileLogin.setCreatedAt(new Date());
            int count = mobileLoginMapper.insert(mobileLogin);
            if(count == 1){
                Map<String, String> map = new HashMap<>();
                map.put("token",token);
                return ResponseResult.ok(map);
            }
        }
        return ResponseResult.error("-1","注册失败！");
    }

    /**
     * 用户登陆
     */
    @Override
    public ResponseResult getUser(String tel, String password) {
        if(StringUtils.isEmpty(tel)){
            return ResponseResult.error("-1","手机号不能为空！");
        }
        List<User> userList = userMapper.selectUserBy(null, tel, "");
        if(userList != null && userList.size()>0){
            User user = userList.get(0);
            if (PasswordUtil.generate(password, user.getSalt()).equals(user.getPassword())){
                String token = CommTool.getUUID();
                //记录登陆信息
                MobileLogin mobileLogin = new MobileLogin();
                mobileLogin.setuId(user.getuId());
                mobileLogin.setToken(token);
                mobileLogin.setLastAccessTime(System.currentTimeMillis());
                mobileLogin.setStatus(1);
                mobileLogin.setCreatedAt(new Date());
                int count = mobileLoginMapper.insert(mobileLogin);
                if(count == 1){
                    Map<String, Object> map = new HashMap<>();
                    map.put("token",token);
                    map.put("user",user);
                    return ResponseResult.ok(map);
                }
            }
            return ResponseResult.error("-3","密码错误！");
        }

        return ResponseResult.error("-2","用户不存在！");
    }


    /**
     * 用户个人资料查询
     */
    @Override
    public ResponseResult getUserInfo(String uId) {
        List<User> userList = userMapper.selectUserBy(uId, null, "");
        if(userList != null && userList.size()==1){
            User user = userList.get(0);
            return ResponseResult.ok(user);
        }
        return ResponseResult.error("-1","用户信息异常！");

    }



    /**
     * 条件查询用户
     */
    @Override
    public List<User> selectUserBy(String uId, String tel, String status) {
        return userMapper.selectUserBy(uId,tel,status);
    }


    /**
     * 用户信息修改
     */
    @Override
    public int updateUser(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }




}
