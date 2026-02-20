package com.easypay.api.dto.mch;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商户基本信息数据传输对象
 */
public class MchInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 商户编号
    private String mchNo;
    // 商户名称
    private String mchName;
    // 商户简称
    private String mchShortName;
    // 商户类型
    private Byte type;
    // 商户状态
    private Byte state;
    // 所属代理商编号
    private String agentNo;
    // 联系人姓名
    private String contactName;
    // 联系人电话
    private String contactTel;
    // 联系人邮箱
    private String contactEmail;
    // 接口密钥
    private String apiKey;
    // 备注
    private String remark;
    // 创建时间
    private LocalDateTime createdAt;
    // 更新时间
    private LocalDateTime updatedAt;

    public MchInfoDTO() {
    }

    public String getMchNo() {
        return mchNo;
    }

    public void setMchNo(String mchNo) {
        this.mchNo = mchNo;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public String getMchShortName() {
        return mchShortName;
    }

    public void setMchShortName(String mchShortName) {
        this.mchShortName = mchShortName;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
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
