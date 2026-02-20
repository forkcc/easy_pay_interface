package com.easypay.provider.entity.account;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_sett_record")
public class SettRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sett_no", length = 30, nullable = false, unique = true)
    private String settNo;

    @Column(name = "account_no", length = 30)
    private String accountNo;

    @Column(name = "mch_no", length = 30)
    private String mchNo;

    @Column(name = "agent_no", length = 30)
    private String agentNo;

    @Column(name = "sett_amount", nullable = false)
    private Long settAmount;

    @Column(name = "fee_amount")
    private Long feeAmount = 0L;

    @Column(name = "remit_amount")
    private Long remitAmount;

    @Column(name = "state", nullable = false)
    private Byte state;

    @Column(name = "bank_account_no", length = 30)
    private String bankAccountNo;

    @Column(name = "bank_account_name", length = 60)
    private String bankAccountName;

    @Column(name = "bank_name", length = 60)
    private String bankName;

    @Column(name = "err_msg", length = 256)
    private String errMsg;

    @Column(name = "success_time")
    private LocalDateTime successTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSettNo() {
        return settNo;
    }

    public void setSettNo(String settNo) {
        this.settNo = settNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getMchNo() {
        return mchNo;
    }

    public void setMchNo(String mchNo) {
        this.mchNo = mchNo;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public Long getSettAmount() {
        return settAmount;
    }

    public void setSettAmount(Long settAmount) {
        this.settAmount = settAmount;
    }

    public Long getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Long feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Long getRemitAmount() {
        return remitAmount;
    }

    public void setRemitAmount(Long remitAmount) {
        this.remitAmount = remitAmount;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public LocalDateTime getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
    }
}
