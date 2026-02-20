package com.easypay.api.dto.pay;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PayInterfaceConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String infoType;
    private String infoId;
    private String ifCode;
    private String ifParams;
    private Byte state;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PayInterfaceConfigDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getIfCode() {
        return ifCode;
    }

    public void setIfCode(String ifCode) {
        this.ifCode = ifCode;
    }

    public String getIfParams() {
        return ifParams;
    }

    public void setIfParams(String ifParams) {
        this.ifParams = ifParams;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
