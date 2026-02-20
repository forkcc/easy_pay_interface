package com.easypay.api.dto.pay;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付接口定义数据传输对象
 */
public class PayInterfaceDefineDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 接口编码
    private String ifCode;
    // 接口名称
    private String ifName;
    // 接口类型
    private Byte ifType;
    // 配置页面类型
    private String configPageType;
    // 图标
    private String icon;
    // 背景颜色
    private String bgColor;
    // 状态
    private Byte state;
    // 备注
    private String remark;
    // 配置信息
    private String configInfo;
    // 创建时间
    private LocalDateTime createdAt;
    // 更新时间
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
