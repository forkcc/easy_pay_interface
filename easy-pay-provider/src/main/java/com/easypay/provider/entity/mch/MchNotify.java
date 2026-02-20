package com.easypay.provider.entity.mch;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_mch_notify")
public class MchNotify extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notify_id")
    private Long notifyId;

    @Column(name = "order_id", length = 30)
    private String orderId;

    @Column(name = "order_type", length = 20)
    private String orderType;

    @Column(name = "mch_no", length = 30)
    private String mchNo;

    @Column(name = "mch_order_no", length = 30)
    private String mchOrderNo;

    @Column(name = "notify_url", length = 256)
    private String notifyUrl;

    @Column(name = "state")
    private Byte state;

    @Column(name = "notify_count", columnDefinition = "int default 0")
    private Integer notifyCount;

    @Column(name = "notify_count_limit")
    private Long notifyCountLimit;

    @Column(name = "last_notify_time")
    private LocalDateTime lastNotifyTime;

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
}
