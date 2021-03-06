package com.jyss.bacon.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private Integer uId;           //用户id

    private String account;       //帐号

    private String tel;          //手机号

    private String password;       //密码

    private String salt;         //加密盐

    private String payPwd;        //支付密码

    private String nick;         //昵称

    private String headpic;       //头像

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthDate;       //出生日期

    private Integer sex;          //1女，2男

    private Integer age;          //年龄

    private String place;         //出没地

    private Integer emotionalState;       //1单身，2未婚，3已婚

    private String profession;         //职业

    private String hobby;           //爱好

    private Integer status;         //0禁用，1普通用户，2陪玩用户，3明星用户

    private Integer source;         //0直接注册，1微信，2QQ

    private Integer isAuth;         //0未认证，1=已认证

    private Integer popularity;       //热度

    private String labelDesc;         //明星描述

    private Float balance;           //充值培根币金额，只可用于消费

    private Float amount;            //可提现培根币金额，只用于提现

    private Date createTime;          //创建时间

    private Date lastModifyTime;       //修改时间

    private String money;              //显示充值培根币金额

    private String openId;            //扣扣openid
    private String unionId;           //微信union_id

    private String accountWy;         //网易通讯登陆账号
    private String tokenWy;           //网易通讯登陆密码


    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick == null ? null : nick.trim();
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic == null ? null : headpic.trim();
    }

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place == null ? null : place.trim();
    }

    public Integer getEmotionalState() {
        return emotionalState;
    }

    public void setEmotionalState(Integer emotionalState) {
        this.emotionalState = emotionalState;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession == null ? null : profession.trim();
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby == null ? null : hobby.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

    public String getLabelDesc() {
        return labelDesc;
    }

    public void setLabelDesc(String labelDesc) {
        this.labelDesc = labelDesc;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getAccountWy() {
        return accountWy;
    }

    public void setAccountWy(String accountWy) {
        this.accountWy = accountWy;
    }

    public String getTokenWy() {
        return tokenWy;
    }

    public void setTokenWy(String tokenWy) {
        this.tokenWy = tokenWy;
    }
}