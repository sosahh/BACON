package com.jyss.bacon.entity;

import java.io.Serializable;
import java.util.Date;

public class MobileLogin implements Serializable {
    private Long id;

    private Integer uId;     //用户id

    private String token;       //口令

    private Date createdAt;

    private Long lastAccessTime;     //最后访问时间

    private String pushInfo;     //推送信息

    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getPushInfo() {
        return pushInfo;
    }

    public void setPushInfo(String pushInfo) {
        this.pushInfo = pushInfo == null ? null : pushInfo.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}