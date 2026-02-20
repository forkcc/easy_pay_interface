package com.easypay.api.dto.pay;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 转账订单数据传输对象
 */
public class TransferOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 转账订单号
    private String transferId;
    // 商户编号
    private String mchNo;
    // 应用ID
    private String appId;
    // 商户转账单号
    private String mchTransferNo;
    // 支付方式编码
    private String wayCode;
    // 转账金额（单位：分）
    private Long amount;
    // 币种
    private String currency;
    // 转账状态
    private Byte state;
    // 收款账号
    private String accountNo;
    // 收款人姓名
    private String accountName;
    // 收款银行名称
    private String bankName;
    // 渠道订单号
    private String channelOrderNo;
    // 错误码
    private String errCode;
    // 错误信息
    private String errMsg;
    // 扩展参数
    private String extParam;
    // 转账成功时间
    private LocalDateTime successTime;
    // 创建时间
    private LocalDateTime createdAt;
    // 更新时间
    private LocalDateTime updatedAt;

    public TransferOrderDTO() {
    }

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
