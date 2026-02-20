package com.easypay.api.dto.mch;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MchNotifyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long notifyId;
    private String orderId;
    private String orderType;
    private String mchNo;
    private String mchOrderNo;
    private String notifyUrl;
    private Byte state;
    private Integer notifyCount;
    private Long notifyCountLimit;
    private LocalDateTime lastNotifyTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MchNotifyDTO() {
    }

    public Long getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(Long notifyId) {
        this.notifyId = notifyId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getMchNo() {
        return mchNo;
    }

    public void setMchNo(String mchNo) {
        this.mchNo = mchNo;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Integer getNotifyCount() {
        return notifyCount;
    }

    public void setNotifyCount(Integer notifyCount) {
        this.notifyCount = notifyCount;
    }

    public Long getNotifyCountLimit() {
        return notifyCountLimit;
    }

    public void setNotifyCountLimit(Long notifyCountLimit) {
        this.notifyCountLimit = notifyCountLimit;
    }

    public LocalDateTime getLastNotifyTime() {
        return lastNotifyTime;
    }

    public void setLastNotifyTime(LocalDateTime lastNotifyTime) {
        this.lastNotifyTime = lastNotifyTime;
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
