package com.easypay.api.dto.pay;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付接口配置数据传输对象
 */
public class PayInterfaceConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 配置ID
    private Long id;
    // 账号类型
    private String infoType;
    // 账号ID
    private String infoId;
    // 支付接口编码
    private String ifCode;
    // 接口参数配置
    private String ifParams;
    // 状态
    private Byte state;
    // 备注
    private String remark;
    // 创建时间
    private LocalDateTime createdAt;
    // 更新时间
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
