package com.jyss.bacon.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderSfView implements Serializable {
    private Integer id;

    private String orderId;        //订单号

    private Integer uId;             //用户id

    private String area;           //微信区，qq区，其它区

    private String type;              //排位赛，匹配赛

    private Integer levelId;             //段位id

    private Integer count;           //局数

    private Double price;              //单价

    private Double total;            //合计

    private Integer isWin;           //0不计胜负，1计胜负

    private String account;          //账号

    private String wxAccount;         //微信账号

    private Integer status;          //0未支付，1已支付，2已接单，3完成

    private Date created;

    private Date modifyTime;
    private double acceptTime;///接单小时数
    
    private String cjsj;// 创建时间
	private String xgsj;// 最新修改时间
	
	//////////视图字段
	private Integer oid;///订单表自增id 
	private Integer rid;///结果表自增id
    private Integer sfUserId;///上分人员ID
	private String uaccount;   //上分人员账号
	private String uname;   //上分人员姓名
	private String picture;   //图片
    private String [] pictures;     //结果图片
	private String result;   //结果
	private String sfStar;   //上星
    private Integer reStatus ;///1 =分配订单 2=完成订单 3=取消订单

    /////用户信息/////,c.category_name,c.dw_name,c.name,u.tel,u.nick,u.headpic
    private String categoryName;   //游戏
    private String dwName;   //大段位
    private String name;   //小段位
    private String tel;   //下单人电话
    private String nick;   //昵称
    private String headpic;   //下单用户头像

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDwName() {
        return dwName;
    }

    public void setDwName(String dwName) {
        this.dwName = dwName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public double getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(double acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Integer getSfUserId() {
        return sfUserId;
    }

    public void setSfUserId(Integer sfUserId) {
        this.sfUserId = sfUserId;
    }

    ///private List<String> pictures;     //结果图片
    private Date finishTime;//// 完成时间
    private String gameAccount;///游戏账号
    private String gamePwd;///游戏密码

    private  double finishMoney;////预计完成收入金额




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
 

    public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public Integer getIsWin() {
        return isWin;
    }

    public void setIsWin(Integer isWin) {
        this.isWin = isWin;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getWxAccount() {
        return wxAccount;
    }

    public void setWxAccount(String wxAccount) {
        this.wxAccount = wxAccount == null ? null : wxAccount.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8")
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public String getXgsj() {
		return xgsj;
	}

	public void setXgsj(String xgsj) {
		this.xgsj = xgsj;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public String getUaccount() {
		return uaccount;
	}

	public void setUaccount(String uaccount) {
		this.uaccount = uaccount;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
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

    public Integer getReStatus() {
        return reStatus;
    }

    public void setReStatus(Integer reStatus) {
        this.reStatus = reStatus;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8")
    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getGameAccount() {
        return gameAccount;
    }

    public void setGameAccount(String gameAccount) {
        this.gameAccount = gameAccount;
    }

    public String getGamePwd() {
        return gamePwd;
    }

    public void setGamePwd(String gamePwd) {
        this.gamePwd = gamePwd;
    }

    public double getFinishMoney() {
        return finishMoney;
    }

    public void setFinishMoney(double finishMoney) {
        this.finishMoney = finishMoney;
    }
}