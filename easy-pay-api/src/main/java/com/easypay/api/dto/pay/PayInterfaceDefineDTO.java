package com.easypay.api.dto.pay;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PayInterfaceDefineDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ifCode;
    private String ifName;
    private Byte ifType;
    private String configPageType;
    private String icon;
    private String bgColor;
    private Byte state;
    private String remark;
    private String configInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PayInterfaceDefineDTO() {
    }

    public String getIfCode() {
        return ifCode;
    }

    public void setIfCode(String ifCode) {
        this.ifCode = ifCode;
    }

    public String getIfName() {
        return ifName;
    }

    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    public Byte getIfType() {
        return ifType;
    }

    public void setIfType(Byte ifType) {
        this.ifType = ifType;
    }

    public String getConfigPageType() {
        return configPageType;
    }

    public void setConfigPageType(String configPageType) {
        this.configPageType = configPageType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
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

    public String getConfigInfo() {
        return configInfo;
    }

    public void setConfigInfo(String configInfo) {
        this.configInfo = configInfo;
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
