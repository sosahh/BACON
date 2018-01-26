package com.jyss.bacon.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.bacon.entity.*;
import com.jyss.bacon.mapper.*;
import com.jyss.bacon.service.UserService;
import com.jyss.bacon.utils.CommTool;
import com.jyss.bacon.utils.PasswordUtil;
import com.jyss.bacon.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MobileLoginMapper mobileLoginMapper;
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private ScoreBalanceMapper scoreBalanceMapper;
    @Autowired
    private XtclMapper xtclMapper;



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
                return ResponseResult.error("-4","登陆失败！");
            }
            return ResponseResult.error("-3","密码错误！");
        }
        return ResponseResult.error("-2","用户不存在！");
    }


    /**
     * 用户第三方登陆
     */
    @Override
    public ResponseResult getUserByOpenId(String openId, String unionId) {
        List<User> userList = userMapper.selectUserByOpenId(openId, unionId);
        if(userList != null && userList.size()==1){
            User user = userList.get(0);
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
        return ResponseResult.error("-4","登陆失败！");
    }


    /**
     * 用户个人资料查询
     */
    @Override
    public ResponseResult getUserInfo(String uId) {
        Map<String, Object> map = new HashMap<>();
        List<User> userList = userMapper.selectUserBy(uId, null, "");
        if(userList != null && userList.size()==1){
            User user = userList.get(0);
            List<UserAuth> authList = userAuthMapper.getUserAuthBy(Integer.valueOf(uId), null, 2);
            if(StringUtils.isEmpty(user.getPayPwd())){
                map.put("isPay",false);
            }else {
                map.put("isPay",true);
            }
            map.put("user",user);
            map.put("auth",authList);
            return ResponseResult.ok(map);
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


    /**
     * 我的收入
     * @param uId
     * @return
     */
    @Override
    public ResponseResult selectUserWallet(Integer uId) {
        List<User> userList = userMapper.selectUserBy(uId + "", null, null);
        if(userList != null && userList.size()== 1){

            User user = userList.get(0);
            String totalIncome = scoreBalanceMapper.getTotalIncome(uId);
            String incomeToday = scoreBalanceMapper.getIncomeToday(uId);
            List<Xtcl> xtclList = xtclMapper.getClsBy("cash_type", "1");
            Xtcl xtcl = xtclList.get(0);

            List<Xtcl> xtclList1 = xtclMapper.getClsBy("prop_type", "1");
            Xtcl xtcl1 = xtclList1.get(0);
            double prop = Double.parseDouble(xtcl1.getBz_value());
            float cash = (float) (Math.round((user.getAmount() / prop) * 100)) / 100;

            HashMap<String, Object> map = new HashMap<>();
            map.put("total",totalIncome);
            map.put("today",incomeToday);
            map.put("cash",cash);
            map.put("money",xtcl.getBz_value());
            return ResponseResult.ok(map);
        }
        return ResponseResult.error("-1","用户信息异常！");
    }


    /**
     * 我的培根币账单
     * @param uId
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult selectScoreBalance(Integer uId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<ScoreBalance> list = scoreBalanceMapper.selectMyScoreBalance(uId);
        PageInfo<ScoreBalance> pageInfo = new PageInfo<>(list);
        Page<ScoreBalance> result = new Page<>(pageInfo);

        return ResponseResult.ok(result);
    }


    /**
     * 收入账单
     * @param uId
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult selectMoneyDetail(Integer uId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<ScoreEarn> list = scoreBalanceMapper.selectScoreEarn(uId);
        PageInfo<ScoreEarn> pageInfo = new PageInfo<>(list);
        Page<ScoreEarn> result = new Page<>(pageInfo);

        return ResponseResult.ok(result);
    }


    /**
     * 我的培根币
     * @param uId
     * @return
     */
    @Override
    public ResponseResult selectUserBalance(Integer uId) {
        List<User> userList = userMapper.selectUserBy(uId + "", null, null);
        if(userList != null && userList.size()== 1){
            User user = userList.get(0);
            List<Xtcl> xtclList = xtclMapper.getClsBy("prop_type", "1");
            Xtcl xtcl = xtclList.get(0);
            double prop = Double.parseDouble(xtcl.getBz_value());              //比例
            List<Xtcl> xtclList1 = xtclMapper.getClsBy("money_type", null);
            List<Object> list = new ArrayList<>();
            for (Xtcl xtcl1 : xtclList1) {
                double cash = Double.parseDouble(xtcl1.getBz_value());
                Map<String, Double> map = new HashMap<>();
                map.put("cash",cash);
                map.put("total",cash*prop);
                list.add(map);
            }
            Map<Object, Object> map1 = new LinkedHashMap<>();
            map1.put("balance",user.getMoney());
            map1.put("items",list);
            return ResponseResult.ok(map1);
        }
        return ResponseResult.error("-1","用户信息异常！");
    }


}
