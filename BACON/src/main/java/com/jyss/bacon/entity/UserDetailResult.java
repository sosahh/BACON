package com.jyss.bacon.entity;

import java.io.Serializable;
import java.util.List;

public class UserDetailResult implements Serializable{

    private Integer uId;       //用户id
    private User user;
    private Integer count;     //关注数
    private Boolean type;      //是否已关注   true=已关注，false=未关注
    private List<UserDynamic>  pictures;       //动态图片
    private List<UserAuth> games;     //认证游戏


    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

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

    public List<UserAuth> getGames() {
        return games;
    }

    public void setGames(List<UserAuth> games) {
        this.games = games;
    }
}
