package com.jyss.bacon.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class OrderPw implements Serializable {
    private Integer id;

    private String orderId;       //订单号

    private Integer uId;          //用户id

    private Integer playId;        //陪玩人id

    private Integer categoryId;      //类目id

    private String categoryTitle;     //类目名称

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Date appointTime;        //预约时间

    private Integer count;        //局数

    private String remarks;       //备注

    private Double price;         //单价

    private Double total;         //总计

    private Integer status;       //0未支付，1已支付，2已接单，3进行中，4完成，5订单取消

    private Integer orderReason;     //0正常，1客户取消，2约客取消

    private Integer type;           //0，1陪玩订单，2约客订单

    private Date created;         //创建时间

    private Date singleTime;       //接单时间

    private Date modifyTime;        //订单修改时间

    private Integer isPj;           //是否评价   0未评价，1已评价
    private String nick;            //昵称
    private String headpic;         //头像



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

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getPlayId() {
        return playId;
    }

    public void setPlayId(Integer playId) {
        this.playId = playId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle == null ? null : categoryTitle.trim();
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8")
    public Date getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(Date appointTime) {
        this.appointTime = appointTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrderReason() {
        return orderReason;
    }

    public void setOrderReason(Integer orderReason) {
        this.orderReason = orderReason;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getSingleTime() {
        return singleTime;
    }

    public void setSingleTime(Date singleTime) {
        this.singleTime = singleTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public Integer getIsPj() {
        return isPj;
    }

    public void setIsPj(Integer isPj) {
        this.isPj = isPj;
    }
}