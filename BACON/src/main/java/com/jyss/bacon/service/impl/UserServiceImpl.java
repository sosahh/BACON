package com.jyss.bacon.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.bacon.config.AliConfig;
import com.jyss.bacon.constant.Constant;
import com.jyss.bacon.entity.*;
import com.jyss.bacon.mapper.*;
import com.jyss.bacon.service.UserService;
import com.jyss.bacon.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Id;
import java.text.SimpleDateFormat;
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
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private UserReportMapper userReportMapper;
    @Autowired
    private UserDynamicMapper userDynamicMapper;




    /**
     * 用户注册
     */
    @Override
    public ResponseResult insert(User user) {
        int insert = userMapper.insert(user);
        if(insert == 1){
            Integer uId = user.getuId();
            String account = Utils.getMyId(uId + "");
            Map<String, String> map1 = WangyiyunUtils.signWangyiyun(account,
                    user.getNick(),Constant.httpUrl+user.getHeadpic());
            if(map1.get("code").equals("200")){
                //设置账号
                User user1 = new User();
                user1.setuId(uId);
                user1.setAccount(account);
                user1.setAccountWy(account);
                user1.setTokenWy(map1.get("token"));
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
                    map.put("accountWy",account);
                    map.put("tokenWy",map1.get("token"));
                    return ResponseResult.ok(map);
                }
            }

        }
        return ResponseResult.error("-1","注册失败！");
    }

    /**
     * 用户登陆
     */
    @Override
    public ResponseResult getUser(String tel, String password) {
        if(StringUtils.isEmpty(tel)) return ResponseResult.error("-1", "手机号不能为空！");
        List<User> userList = userMapper.selectUserBy(null, tel, "");
        if(userList != null && userList.size()>0){
            User user = userList.get(0);
            if(StringUtils.isEmpty(user.getAccountWy())&& StringUtils.isEmpty(user.getTokenWy())){
                Map<String, String> map1 = WangyiyunUtils.signWangyiyun(user.getAccount(),
                        user.getNick(), user.getHeadpic());
                if(map1.get("code").equals("200")){
                    //设置账号
                    User user1 = new User();
                    user1.setuId(user.getuId());
                    user1.setAccountWy(user.getAccount());
                    user1.setTokenWy(map1.get("token"));
                    userMapper.updateByPrimaryKeySelective(user1);
                }
            }
            List<User> userList1 = userMapper.selectUserBy(null, tel, "");
            User user1 = userList1.get(0);
            if (PasswordUtil.generate(password, user1.getSalt()).equals(user1.getPassword())){
                String token = CommTool.getUUID();
                //记录登陆信息
                MobileLogin mobileLogin = new MobileLogin();
                mobileLogin.setuId(user1.getuId());
                mobileLogin.setToken(token);
                mobileLogin.setLastAccessTime(System.currentTimeMillis());
                mobileLogin.setStatus(1);
                mobileLogin.setCreatedAt(new Date());
                int count = mobileLoginMapper.insert(mobileLogin);
                if(count == 1){
                    Map<String, Object> map = new HashMap<>();
                    map.put("token",token);
                    map.put("user",user1);
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
            List<Xtcl> xtclList = xtclMapper.getClsBy("cash_type", "1");       //单笔最低提现金额
            Xtcl xtcl = xtclList.get(0);
            List<Xtcl> xtclList2 = xtclMapper.getClsBy("cash_type", "2");       //单笔最高提现金额
            Xtcl xtcl2 = xtclList2.get(0);

            List<Xtcl> xtclList1 = xtclMapper.getClsBy("prop_type", "1");      //比例
            Xtcl xtcl1 = xtclList1.get(0);
            double prop = Double.parseDouble(xtcl1.getBz_value());
            float cash = (float) (Math.round((user.getAmount() / prop) * 100)) / 100;

            HashMap<String, Object> map = new HashMap<>();
            map.put("total",totalIncome);
            map.put("today",incomeToday);
            map.put("amount",user.getAmount());
            map.put("cash",cash);
            map.put("money",xtcl.getBz_value());
            map.put("hmoney",xtcl2.getBz_value());
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
                Map<String, Object> map = new HashMap<>();
                map.put("id",xtcl1.getBz_id());
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

    /**
     * 添加账户
     * @param userAccount
     * @return
     */
    @Override
    public int insertUserAccount(UserAccount userAccount) {
        return userAccountMapper.insertUserAccount(userAccount);
    }

    /**
     * 查询账户
     * @param uId
     * @return
     */
    @Override
    public List<UserAccount> getUserAccount(Integer uId,String account) {
        return userAccountMapper.getUserAccount(uId,account);
    }


    /**
     * 更新账户
     * @param userAccount
     * @return
     */
    @Override
    public int updateUserAccount(UserAccount userAccount) {
        return userAccountMapper.updateByPrimaryKeySelective(userAccount);
    }


    /**
     * 系统消息
     * @param uId
     * @return
     */
    @Override
    public ResponseResult getUserReport(Integer uId,Integer page,Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<UserReport> reports = userReportMapper.getUserReport(uId);
        for (UserReport report : reports) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = sdf.format(report.getCreateTime());
            if(report.getStatus() == 1){           //举报
                List<UserDynamic> dynamicList = userDynamicMapper.getUserDynamicById(report.getDynamicId());
                if(dynamicList != null && dynamicList.size()==1){
                    UserDynamic userDynamic = dynamicList.get(0);
                    report.setReportName("您于"+date+"对"
                            +userDynamic.getNick()+"的举报，我们已经处理，感谢您的支持~");
                }else{
                    report.setReportName("您于"+date+"提出的举报，我们已经处理，感谢您的支持~");
                }
            }else if(report.getStatus() == 2){      //意见
                report.setReportName("您于"+date+"提出的意见与反馈，我们已受理，感谢您的支持~");
            }
            List<UserMessage> messages = userReportMapper.selectUserMessageBy(uId, report.getId(), report.getStatus());
            if(messages != null && messages.size()>0){
                report.setResult(1);            //已读
            }else{
                report.setResult(0);            //未读
            }
        }
        PageInfo<UserReport> pageInfo = new PageInfo<>(reports);
        Page<UserReport> result = new Page<>(pageInfo);
        return ResponseResult.ok(result);
    }


    /**
     * 添加已读消息
     * @param userMessage
     * @return
     */
    @Override
    public int insertUserMessage(UserMessage userMessage) {
        return userReportMapper.insertUserMessage(userMessage);
    }


    /**
     * 用户提现
     * @param uId
     * @param account
     * @param cash
     * @return
     */
    @Override
    public ResponseResult insertScoreEarn(Integer uId, String account, Float cash, String payPwd, String realName) {
        List<User> userList = userMapper.selectUserBy(uId + "", null, null);
        if(userList != null && userList.size()==1){
            User user = userList.get(0);
            if(DigestUtils.md5DigestAsHex(payPwd.getBytes()).equals(user.getPayPwd())){
                List<Xtcl> xtclList = xtclMapper.getClsBy("cash_type", "2");       //最高提现金额
                Xtcl xtcl = xtclList.get(0);
                List<Xtcl> xtclList1 = xtclMapper.getClsBy("cash_type", "1");      //最低提现金额
                Xtcl xtcl1 = xtclList1.get(0);
                List<Xtcl> xtclList2 = xtclMapper.getClsBy("cash_type", "3");      //手续费
                Xtcl xtcl2 = xtclList2.get(0);
                List<Xtcl> xtclList3 = xtclMapper.getClsBy("prop_type", "1");      //比例
                Xtcl xtcl3 = xtclList3.get(0);
                float cash1 = Float.parseFloat(xtcl.getBz_value());
                float cash2 = Float.parseFloat(xtcl1.getBz_value());
                float cash3 = Float.parseFloat(xtcl2.getBz_value());
                float prop = Float.parseFloat(xtcl3.getBz_value());
                if(cash*prop <= user.getAmount()){

                    if(cash >= cash2){
                        if(cash <= cash1){

                            float jyScore = user.getAmount() - cash*prop;
                            User user1 = new User();
                            user1.setuId(uId);
                            user1.setAmount(jyScore);
                            user1.setLastModifyTime(new Date());
                            int count = userMapper.updateByPrimaryKeySelective(user1);
                            if(count == 1){
                                ScoreEarn scoreEarn = new ScoreEarn();
                                scoreEarn.setuId(uId);
                                scoreEarn.setDetail("提现");
                                scoreEarn.setType(2);
                                scoreEarn.setScore((double)cash*prop);
                                scoreEarn.setJyScore((double)jyScore);
                                //scoreEarn.setOrderSn();
                                scoreEarn.setRealName(realName);
                                scoreEarn.setAccount(account);
                                scoreEarn.setStatus(0);
                                scoreEarn.setCreatedAt(new Date());
                                int count1 = scoreBalanceMapper.insertScoreEarn(scoreEarn);
                                if(count1 == 1){
                                    return ResponseResult.ok("");
                                }

                            }
                            return ResponseResult.error("-6","提现失败！");
                        }
                        return ResponseResult.error("-5","提现金额不能超过每笔最高提现金额！");
                    }
                    return ResponseResult.error("-4","提现金额不能低于每笔最低提现金额！");
                }
                return ResponseResult.error("-3","提现金额不能超过可提现金额！");
            }
            return ResponseResult.error("-2","支付密码错误！");
        }
        return ResponseResult.error("-1","用户信息异常！");
    }



    /**
     * 充值结果异步处理
     * @param totalAmount
     * @param orderSn
     * @return
     */
    @Override
    public Boolean updateUserBalance(String totalAmount, String orderSn) {
        List<Xtcl> xtclList = xtclMapper.getClsBy("prop_type", "1");      //比例
        Xtcl xtcl = xtclList.get(0);
        double prop = Double.parseDouble(xtcl.getBz_value());

        List<ScoreBalance> balanceList = scoreBalanceMapper.selectScoreBalance(orderSn);
        if(balanceList != null && balanceList.size()==1){
            ScoreBalance scoreBalance1 = balanceList.get(0);
            Integer uId = scoreBalance1.getuId();
            Double cash = scoreBalance1.getScore();
            if(cash == Double.parseDouble(totalAmount)*prop){
                List<User> userList = userMapper.selectUserBy(uId + "", null, null);
                if(userList != null && userList.size()==1){
                    User user = userList.get(0);
                    double jyScore = user.getBalance() + cash;
                    User user1 = new User();
                    user1.setuId(uId);
                    user1.setBalance((float) jyScore);
                    user1.setLastModifyTime(new Date());
                    int count = userMapper.updateByPrimaryKeySelective(user1);
                    if(count == 1){
                        ScoreBalance scoreBalance = new ScoreBalance();
                        scoreBalance.setId(scoreBalance1.getId());
                        scoreBalance.setJyScore( jyScore);
                        scoreBalance.setStatus(1);
                        int count1 = scoreBalanceMapper.updateByPrimaryKeySelective(scoreBalance);
                        if(count1 == 1){
                            return true;
                        }
                    }
                    return false;
                }
            }
        }

        return false;
    }



    /**
     * 支付宝充值
     */
    @Override
    public ResponseResult getALiPayResult(Integer uId, Float cash) {
        List<Xtcl> xtclList = xtclMapper.getClsBy("prop_type", "1");      //比例
        Xtcl xtcl = xtclList.get(0);
        float prop = Float.parseFloat(xtcl.getBz_value());

        String outTradeNo = DateFormatUtils.getNowDateText("yyMMddHHmmss") + "O" + uId
                + "r" + (long) (Math.random() * 1000L);
        String subject = "虚拟货币消费";
        String totalAmount = cash + "";
        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买培根币消费 " + cash + "元";

        String timeoutExpress = "30m";   // 支付超时，定义为30分钟
        String notifyUrl = Constant.httpUrl + "BACON/AliNotify.action";

        AliConfig config = new AliConfig();

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(config.getURL(), config.getAPP_ID() , config.getAPP_PRIVATE_KEY(),
                "json", "UTF-8", config.getALIPAY_PUBLIC_KEY(), config.getSIGN_TYPE());

        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(body);
        model.setSubject(subject);
        model.setOutTradeNo(outTradeNo);
        model.setTimeoutExpress(timeoutExpress);
        model.setTotalAmount(totalAmount);
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            //System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。

            //创建订单
            ScoreBalance scoreBalance = new ScoreBalance();
            scoreBalance.setuId(uId);
            scoreBalance.setEnd(3);
            scoreBalance.setDetail("充值");
            scoreBalance.setType(1);
            scoreBalance.setScore((double) (cash*prop));
            scoreBalance.setOrderSn(outTradeNo);
            scoreBalance.setStatus(0);
            scoreBalance.setCreatedAt(new Date());
            scoreBalanceMapper.insert(scoreBalance);

            return ResponseResult.ok(response.getBody());
        } catch (AlipayApiException e) {
            return ResponseResult.error("-1","支付异常！");
        }

    }

    ////////////上分人员信息表////////////
    @Override
    public List<UserSf> selectUserSfBy(@Param("id") String id, @Param("account") String account, @Param("status") String status) {
        return userMapper.selectUserSfBy(id,account,status);
    }

    @Override
    public int upUserSf(UserSf userSf) {
        return userMapper.upUserSf(userSf);
    }

    @Override
    public ResponseResult getUserSf(@Param("tel") String tel, @Param("password") String password) {
        if(StringUtils.isEmpty(tel)) return ResponseResult.error("-1", "手机号不能为空！");
        List<UserSf> userList = userMapper.selectUserSfBy(null,tel,"1");
        if(userList != null && userList.size()>0){
            UserSf usersf = userList.get(0);
            if (usersf!=null){
                if (PasswordUtil.generatePayPwd(password).equals(usersf.getPassword())){
                    String token = CommTool.getUUID();
                    //记录登陆信息
                    MobileLogin mobileLogin = new MobileLogin();
                    mobileLogin.setuId(usersf.getId());
                    mobileLogin.setToken(token);
                    mobileLogin.setLastAccessTime(System.currentTimeMillis());
                    mobileLogin.setStatus(1);
                    mobileLogin.setCreatedAt(new Date());
                    int count = mobileLoginMapper.insertSfLogin(mobileLogin);
                    if(count == 1){
                        Map<String, Object> map = new HashMap<>();
                        map.put("token",token);
                        map.put("user",usersf);
                        return ResponseResult.ok(map);
                    }
                    return ResponseResult.error("-4","登陆失败！");
                }else{
                    return ResponseResult.error("-3","密码错误！");
                }
            }else{
                return ResponseResult.error("-2","用户不存在！");
            }

        }
        return ResponseResult.error("-2","用户不存在！");
    }


}
