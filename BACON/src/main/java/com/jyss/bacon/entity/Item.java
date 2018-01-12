package com.jyss.bacon.entity;

import java.io.Serializable;
import java.util.Date;

public class Item implements Serializable{
    private Integer id;

    private String title;      //类目标题

    private String brightPic;        //亮图片

    private String ashPic;        //灰图片

    private Integer status;    //0禁用，1可用

    private Integer sortOrder;     //排列序号

    private Date created;       //创建时间

    private Date updated;       //更新时间


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getBrightPic() {
        return brightPic;
    }

    public void setBrightPic(String brightPic) {
        this.brightPic = brightPic;
    }

    public String getAshPic() {
        return ashPic;
    }

    public void setAshPic(String ashPic) {
        this.ashPic = ashPic;
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

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}