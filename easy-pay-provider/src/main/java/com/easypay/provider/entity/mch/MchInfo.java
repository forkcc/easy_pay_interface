package com.easypay.provider.entity.mch;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_mch_info")
public class MchInfo extends BaseEntity {

    @Id
    @Column(name = "mch_no", length = 30)
    private String mchNo;

    @Column(name = "mch_name", length = 60)
    private String mchName;

    @Column(name = "mch_short_name", length = 30)
    private String mchShortName;

    @Column(name = "type")
    private Byte type;

    @Column(name = "state")
    private Byte state;

    @Column(name = "agent_no", length = 30)
    private String agentNo;

    @Column(name = "contact_name", length = 30)
    private String contactName;

    @Column(name = "contact_tel", length = 20)
    private String contactTel;

    @Column(name = "contact_email", length = 50)
    private String contactEmail;

    @Column(name = "api_key", columnDefinition = "TEXT")
    private String apiKey;

    @Column(name = "remark", length = 256)
    private String remark;

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
}
