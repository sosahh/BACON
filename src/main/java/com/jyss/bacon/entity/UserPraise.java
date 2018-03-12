package com.jyss.bacon.entity;

import java.io.Serializable;
import java.util.Date;

public class UserPraise implements Serializable {
    private Integer id;

    private Integer dynamicId;     //动态id

    private Integer uId;          //评价人id

    private Integer status;       //0禁用，1可用，2新闻

    private Date created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(Integer dynamicId) {
        this.dynamicId = dynamicId;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}