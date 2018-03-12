package com.jyss.bacon.entity;

import java.util.Date;

public class ItemCat {
    private Integer id;

    private Integer categoryId;      //类目id

    private String categoryName;     //类目名称

    private String dwName;       //大段位名称

    private String name;         //小段位名称

    private Double pwPrice;      //排位价格

    private Double yjTime;       //一局时间，单位小时

    private Integer status;      //0禁用，1可用

    private Integer sortOrder;    //排列序号

    private Date created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public String getDwName() {
        return dwName;
    }

    public void setDwName(String dwName) {
        this.dwName = dwName == null ? null : dwName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Double getPwPrice() {
        return pwPrice;
    }

    public void setPwPrice(Double pwPrice) {
        this.pwPrice = pwPrice;
    }

    public Double getYjTime() {
        return yjTime;
    }

    public void setYjTime(Double yjTime) {
        this.yjTime = yjTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}