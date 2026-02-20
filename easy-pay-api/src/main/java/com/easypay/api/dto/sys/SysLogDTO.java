package com.easypay.api.dto.sys;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统操作日志数据传输对象
 */
public class SysLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键ID
    private Long id;
    // 用户类型
    private Byte userType;
    // 用户ID
    private String userId;
    // 用户名称
    private String userName;
    // 系统类型
    private String sysType;
    // 方法名
    private String methodName;
    // 方法描述
    private String methodRemark;
    // 请求地址
    private String reqUrl;
    // 请求参数
    private String optReqParam;
    // 响应信息
    private String optResInfo;
    // 创建时间
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
