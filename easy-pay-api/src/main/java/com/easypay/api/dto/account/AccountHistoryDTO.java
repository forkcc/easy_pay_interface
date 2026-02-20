package com.easypay.api.dto.account;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账户变动历史数据传输对象
 */
public class AccountHistoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键ID
    private Long id;
    // 账户编号
    private String accountNo;
    // 变动类型
    private Byte changeType;
    // 变动金额
    private Long changeAmount;
    // 变动前余额
    private Long balanceBefore;
    // 变动后余额
    private Long balanceAfter;
    // 业务订单号
    private String bizOrderNo;
    // 业务类型
    private String bizType;
    // 备注
    private String remark;
    // 创建时间
    private LocalDateTime createdAt;

    public AccountHistoryDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Byte getChangeType() {
        return changeType;
    }

    public void setChangeType(Byte changeType) {
        this.changeType = changeType;
    }

    public Long getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Long changeAmount) {
        this.changeAmount = changeAmount;
    }

    public Long getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(Long balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public Long getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Long balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getBizOrderNo() {
        return bizOrderNo;
    }

    public void setBizOrderNo(String bizOrderNo) {
        this.bizOrderNo = bizOrderNo;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
