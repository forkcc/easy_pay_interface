package com.easypay.provider.entity.pay;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_transfer_order")
public class TransferOrder extends BaseEntity {

    @Id
    @Column(name = "transfer_id", length = 30)
    private String transferId;

    @Column(name = "mch_no", length = 30, nullable = false)
    private String mchNo;

    @Column(name = "app_id", length = 30)
    private String appId;

    @Column(name = "mch_transfer_no", length = 30)
    private String mchTransferNo;

    @Column(name = "way_code", length = 20)
    private String wayCode;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "state", nullable = false)
    private Byte state;

    @Column(name = "account_no", length = 64)
    private String accountNo;

    @Column(name = "account_name", length = 64)
    private String accountName;

    @Column(name = "bank_name", length = 64)
    private String bankName;

    @Column(name = "channel_order_no", length = 64)
    private String channelOrderNo;

    @Column(name = "err_code", length = 64)
    private String errCode;

    @Column(name = "err_msg", length = 256)
    private String errMsg;

    @Column(name = "ext_param", columnDefinition = "TEXT")
    private String extParam;

    @Column(name = "success_time")
    private LocalDateTime successTime;

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
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

    public String getMchTransferNo() {
        return mchTransferNo;
    }

    public void setMchTransferNo(String mchTransferNo) {
        this.mchTransferNo = mchTransferNo;
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

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
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

    public LocalDateTime getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
    }
}
