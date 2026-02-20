package com.easypay.api.dto.agent;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AgentInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String agentNo;
    private String agentName;
    private Byte level;
    private String parentAgentNo;
    private BigDecimal profitRate;
    private Byte state;
    private String contactName;
    private String contactTel;
    private String contactEmail;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AgentInfoDTO() {
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Byte getLevel() {
        return level;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public String getParentAgentNo() {
        return parentAgentNo;
    }

    public void setParentAgentNo(String parentAgentNo) {
        this.parentAgentNo = parentAgentNo;
    }

    public BigDecimal getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(BigDecimal profitRate) {
        this.profitRate = profitRate;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
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
