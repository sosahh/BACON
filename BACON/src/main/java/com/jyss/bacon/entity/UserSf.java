package com.jyss.bacon.entity;

import java.util.Date;

public class UserSf {
	  private Integer id;
	  private String account;
	  private String wxAccount;///微信账号
	  private String uname;///姓名
	  private Integer sex;
	  private Integer age;
	  private Integer areaId;//游戏区域
	  private String areaName;
	  private Integer categoryId;//游戏类别
	  private String categoryName;
	  private String dw;//段位
	  private Integer status;          //0禁用 1正常
	  private Date createTime;
	  private String cjsj;
	  private String password;/////手机代练端账号登录密码
	  private String zfAccount;
	  private String zfName;
	  private String zfPassword;/////手机代练端提现支付密码
	  private double balance;//个人储蓄账户余额
	  
	  
	public Integer getId() {
		return id;
	}
	public String getCjsj() {
		return cjsj;
	}
	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getWxAccount() {
		return wxAccount;
	}
	public void setWxAccount(String wxAccount) {
		this.wxAccount = wxAccount;
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
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getZfAccount() {
		return zfAccount;
	}
	public void setZfAccount(String zfAccount) {
		this.zfAccount = zfAccount;
	}
	public String getZfName() {
		return zfName;
	}
	public void setZfName(String zfName) {
		this.zfName = zfName;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getZfPassword() {
		return zfPassword;
	}

	public void setZfPassword(String zfPassword) {
		this.zfPassword = zfPassword;
	}
}
