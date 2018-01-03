package com.jyss.bacon.entity;

import java.io.Serializable;

public class UserFollow implements Serializable {
    private Integer id;

    private Integer uId;       //用户id

    private Integer followId;     //关注人id

    private Integer status;      //0禁用，1可用

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getFollowId() {
        return followId;
    }

    public void setFollowId(Integer followId) {
        this.followId = followId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}