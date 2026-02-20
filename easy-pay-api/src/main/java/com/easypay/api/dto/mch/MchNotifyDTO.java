package com.easypay.api.dto.mch;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商户通知记录数据传输对象
 */
public class MchNotifyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 通知ID
    private Long notifyId;
    // 订单ID
    private String orderId;
    // 订单类型
    private String orderType;
    // 商户编号
    private String mchNo;
    // 商户订单号
    private String mchOrderNo;
    // 通知地址
    private String notifyUrl;
    // 通知状态
    private Byte state;
    // 已通知次数
    private Integer notifyCount;
    // 最大通知次数
    private Long notifyCountLimit;
    // 最后一次通知时间
    private LocalDateTime lastNotifyTime;
    // 创建时间
    private LocalDateTime createdAt;
    // 更新时间
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
