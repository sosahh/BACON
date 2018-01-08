package com.jyss.bacon.entity;


import java.io.Serializable;

public class UserInfo implements Serializable{

    private Integer uId;          //用户id
    private String account;       //帐号
    private String nick;          //昵称
    private String headpic;       //头像
    private String labelDesc;         //明星描述
    private String categoryTitle;     //类目名称
    private String titleName;         //小段位名称
    private Double price;         //每局价格
    private Integer sex;          //1女，2男
    private Integer age;          //年龄
    private String titlePwName;      //大段位名称


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
        this.account = account;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getLabelDesc() {
        return labelDesc;
    }

    public void setLabelDesc(String labelDesc) {
        this.labelDesc = labelDesc;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public String getTitlePwName() {
        return titlePwName;
    }

    public void setTitlePwName(String titlePwName) {
        this.titlePwName = titlePwName;
    }
}
