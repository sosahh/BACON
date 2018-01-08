package com.jyss.bacon.entity;

import java.io.Serializable;

public class ResponseResult implements Serializable{

    private Integer status;       //响应状态    1正确，0错误
    private String errorCode;     //错误码
    private String errorMsg;      //错误信息
    private Object data;          //业务数据

    public static ResponseResult ok(Object data){
        return new ResponseResult(data);
    }

    public static ResponseResult error(String errorCode, String errorMsg){
        return new ResponseResult(errorCode,errorMsg);
    }

    public ResponseResult(Object data) {
        this.status = 1;
        this.errorCode = "";
        this.errorMsg = "";
        this.data = data;
    }

    public ResponseResult(String errorCode, String errorMsg) {
        this.status = 0;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = "";
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
