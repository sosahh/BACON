package com.jyss.bacon.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderSfResult implements Serializable {
    private Integer id;

    private String orderId;      //订单号

    private Integer sfUserId;    //代练人id

    private String picture;      //结果图片

    private String result;       //胜负情况

    private String sfStar;       //加星情况

    private Date created;

    private List<String> pictures;     //结果图片



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSfStar() {
        return sfStar;
    }

    public void setSfStar(String sfStar) {
        this.sfStar = sfStar;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getSfUserId() {
        return sfUserId;
    }

    public void setSfUserId(Integer sfUserId) {
        this.sfUserId = sfUserId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
}