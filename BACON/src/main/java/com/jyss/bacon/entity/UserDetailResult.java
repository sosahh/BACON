package com.jyss.bacon.entity;

import java.io.Serializable;
import java.util.List;

public class UserDetailResult implements Serializable{

    private User user;
    private Integer count;     //关注数
    private Boolean type;      //是否已关注   true=已关注，false=未关注
    private List<UserDynamic>  pictures;       //动态图片
    private List<UserAuth> list;     //认证游戏


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public List<UserDynamic> getPictures() {
        return pictures;
    }

    public void setPictures(List<UserDynamic> pictures) {
        this.pictures = pictures;
    }

    public List<UserAuth> getList() {
        return list;
    }

    public void setList(List<UserAuth> list) {
        this.list = list;
    }
}
