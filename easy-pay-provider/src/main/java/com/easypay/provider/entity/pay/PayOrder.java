package com.easypay.provider.entity.pay;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * 支付订单实体，对应表 t_pay_order。
 */
@Entity
@Table(name = "t_pay_order")
public class PayOrder extends BaseEntity {

    @Id
    @Column(name = "pay_order_id", length = 30)
    private String payOrderId;

    @Column(name = "mch_no", length = 30, nullable = false)
    private String mchNo;

    @Column(name = "app_id", length = 30, nullable = false)
    private String appId;

    @Column(name = "mch_order_no", length = 30, nullable = false)
    private String mchOrderNo;

    @Column(name = "way_code", length = 20)
    private String wayCode;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "state", nullable = false)
    private Byte state;

    @Column(name = "client_ip", length = 45)
    private String clientIp;

    @Column(name = "subject", length = 128)
    private String subject;

    @Column(name = "body", length = 256)
    private String body;

    @Column(name = "fee_rate")
    private Long feeRate;

    @Column(name = "fee_amount")
    private Long feeAmount;

    @Column(name = "channel_order_no", length = 64)
    private String channelOrderNo;

    @Column(name = "channel_extra", columnDefinition = "TEXT")
    private String channelExtra;

    @Column(name = "notify_url", length = 256)
    private String notifyUrl;

    @Column(name = "return_url", length = 256)
    private String returnUrl;

    @Column(name = "err_code", length = 64)
    private String errCode;

    @Column(name = "err_msg", length = 256)
    private String errMsg;

    @Column(name = "ext_param", columnDefinition = "TEXT")
    private String extParam;

    @Column(name = "expired_time")
    private LocalDateTime expiredTime;

    @Column(name = "success_time")
    private LocalDateTime successTime;

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

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public String getWayCode() {
        return wayCode;
    }

    public void setWayCode(String wayCode) {
        this.wayCode = wayCode;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
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

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(Long feeRate) {
        this.feeRate = feeRate;
    }

    public Long getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Long feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public String getChannelExtra() {
        return channelExtra;
    }

    public void setChannelExtra(String channelExtra) {
        this.channelExtra = channelExtra;
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

    public String getExtParam() {
        return extParam;
    }

    public void setExtParam(String extParam) {
        this.extParam = extParam;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    public LocalDateTime getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
    }
}
