package com.easypay.provider.entity.agent;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * 代理商信息实体，对应表 t_agent_info。
 */
@Entity
@Table(name = "t_agent_info")
public class AgentInfo extends BaseEntity {

    @Id
    @Column(name = "agent_no", length = 30)
    private String agentNo;

    @Column(name = "agent_name", length = 60)
    private String agentName;

    @Column(name = "level")
    private Byte level;

    @Column(name = "parent_agent_no", length = 30)
    private String parentAgentNo;

    @Column(name = "profit_rate", precision = 10, scale = 4)
    private BigDecimal profitRate;

    @Column(name = "state")
    private Byte state;

    @Column(name = "contact_name", length = 30)
    private String contactName;

    @Column(name = "contact_tel", length = 20)
    private String contactTel;

    @Column(name = "contact_email", length = 50)
    private String contactEmail;

    @Column(name = "remark", length = 256)
    private String remark;

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

    public java.math.BigDecimal getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(java.math.BigDecimal profitRate) {
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
}
