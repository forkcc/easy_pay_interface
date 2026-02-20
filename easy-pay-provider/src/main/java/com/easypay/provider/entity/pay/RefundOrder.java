package com.easypay.provider.entity.pay;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_refund_order")
public class RefundOrder extends BaseEntity {

    @Id
    @Column(name = "refund_order_id", length = 30)
    private String refundOrderId;

    @Column(name = "pay_order_id", length = 30, nullable = false)
    private String payOrderId;

    @Column(name = "mch_no", length = 30, nullable = false)
    private String mchNo;

    @Column(name = "app_id", length = 30)
    private String appId;

    @Column(name = "mch_refund_no", length = 30)
    private String mchRefundNo;

    @Column(name = "pay_amount")
    private Long payAmount;

    @Column(name = "refund_amount", nullable = false)
    private Long refundAmount;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "state", nullable = false)
    private Byte state;

    @Column(name = "channel_order_no", length = 64)
    private String channelOrderNo;

    @Column(name = "channel_refund_no", length = 64)
    private String channelRefundNo;

    @Column(name = "reason", length = 256)
    private String reason;

    @Column(name = "err_code", length = 64)
    private String errCode;

    @Column(name = "err_msg", length = 256)
    private String errMsg;

    @Column(name = "notify_url", length = 256)
    private String notifyUrl;

    @Column(name = "ext_param", columnDefinition = "TEXT")
    private String extParam;

    @Column(name = "success_time")
    private LocalDateTime successTime;

    public String getRefundOrderId() {
        return refundOrderId;
    }

    public void setRefundOrderId(String refundOrderId) {
        this.refundOrderId = refundOrderId;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getMchNo() {
        return mchNo;
    }

    public void setMchNo(String mchNo) {
        this.mchNo = mchNo;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchRefundNo() {
        return mchRefundNo;
    }

    public void setMchRefundNo(String mchRefundNo) {
        this.mchRefundNo = mchRefundNo;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }

    public Long getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public String getChannelRefundNo() {
        return channelRefundNo;
    }

    public void setChannelRefundNo(String channelRefundNo) {
        this.channelRefundNo = channelRefundNo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getExtParam() {
        return extParam;
    }

    public void setExtParam(String extParam) {
        this.extParam = extParam;
    }

    public LocalDateTime getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
    }
}
