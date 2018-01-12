package com.jyss.bacon.entity;

import java.util.Date;

public class BaseNew {
    private Integer id;

    private String newPic;     //新闻图片

    private String headTitle;   //大标题

    private String subTitle;    //小标题

    private String source;      //图片来源

    private String cooperation;     //投稿合作

    private Integer status;       //0=禁用，1=可用

    private Date createTime;       //

    private String content;       //内容



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNewPic() {
        return newPic;
    }

    public void setNewPic(String newPic) {
        this.newPic = newPic == null ? null : newPic.trim();
    }

    public String getHeadTitle() {
        return headTitle;
    }

    public void setHeadTitle(String headTitle) {
        this.headTitle = headTitle == null ? null : headTitle.trim();
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle == null ? null : subTitle.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getCooperation() {
        return cooperation;
    }

    public void setCooperation(String cooperation) {
        this.cooperation = cooperation == null ? null : cooperation.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}