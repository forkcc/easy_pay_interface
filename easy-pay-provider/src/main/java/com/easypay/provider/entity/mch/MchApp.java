package com.easypay.provider.entity.mch;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_mch_app")
public class MchApp extends BaseEntity {

    @Id
    @Column(name = "app_id", length = 30)
    private String appId;

    @Column(name = "mch_no", length = 30, nullable = false)
    private String mchNo;

    @Column(name = "app_name", length = 60)
    private String appName;

    @Column(name = "state")
    private Byte state;

    @Column(name = "app_secret", columnDefinition = "TEXT")
    private String appSecret;

    @Column(name = "notify_url", length = 256)
    private String notifyUrl;

    @Column(name = "return_url", length = 256)
    private String returnUrl;

    @Column(name = "remark", length = 256)
    private String remark;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchNo() {
        return mchNo;
    }

    public void setMchNo(String mchNo) {
        this.mchNo = mchNo;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
