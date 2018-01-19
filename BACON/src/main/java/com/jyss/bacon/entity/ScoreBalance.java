package com.jyss.bacon.entity;

import java.util.Date;

public class ScoreBalance {
    private Integer id;

    private Integer end;         //1=上分 2=陪玩

    private Integer uId;         //用户id

    private String detail;        //补充说明

    private Integer type;        //1=收入 2=支出

    private Double score;        //积分数额

    private Double jyScore;       //结余数额

    private String orderSn;       //订单号

    private Date createdAt;

    private Integer status;      //1=正常



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getJyScore() {
        return jyScore;
    }

    public void setJyScore(Double jyScore) {
        this.jyScore = jyScore;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn == null ? null : orderSn.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}