package com.easypay.provider.entity.sys;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_sys_log")
public class SysLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_type")
    private Byte userType;

    @Column(name = "user_id", length = 30)
    private String userId;

    @Column(name = "user_name", length = 30)
    private String userName;

    @Column(name = "sys_type", length = 20)
    private String sysType;

    @Column(name = "method_name", length = 128)
    private String methodName;

    @Column(name = "method_remark", length = 128)
    private String methodRemark;

    @Column(name = "req_url", length = 256)
    private String reqUrl;

    @Column(name = "opt_req_param", columnDefinition = "TEXT")
    private String optReqParam;

    @Column(name = "opt_res_info", columnDefinition = "TEXT")
    private String optResInfo;

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
}
