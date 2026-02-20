package com.easypay.api.dto.sys;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SysLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Byte userType;
    private String userId;
    private String userName;
    private String sysType;
    private String methodName;
    private String methodRemark;
    private String reqUrl;
    private String optReqParam;
    private String optResInfo;
    private LocalDateTime createdAt;

    public SysLogDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getUserType() {
        return userType;
    }

    public void setUserType(Byte userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodRemark() {
        return methodRemark;
    }

    public void setMethodRemark(String methodRemark) {
        this.methodRemark = methodRemark;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getOptReqParam() {
        return optReqParam;
    }

    public void setOptReqParam(String optReqParam) {
        this.optReqParam = optReqParam;
    }

    public String getOptResInfo() {
        return optResInfo;
    }

    public void setOptResInfo(String optResInfo) {
        this.optResInfo = optResInfo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
