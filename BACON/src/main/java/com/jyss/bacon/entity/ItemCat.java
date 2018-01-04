package com.jyss.bacon.entity;

import java.io.Serializable;
import java.util.Date;

public class ItemCat implements Serializable {
    private Integer id;

    private Integer categoryId;     //类目id

    private Integer parentId;

    private String name;         //段位名称

    private Double pwPrice;        //排位价格

    private Double ppPrice;        //匹配价格

    private Integer status;      //0禁用，1可用

    private Integer isParent;     //0=不是父级，1=是父级

    private Integer sortOrder;      //排列序号

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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
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

    public Double getPwPrice() {
        return pwPrice;
    }

    public void setPwPrice(Double pwPrice) {
        this.pwPrice = pwPrice;
    }

    public Double getPpPrice() {
        return ppPrice;
    }

    public void setPpPrice(Double ppPrice) {
        this.ppPrice = ppPrice;
    }
}