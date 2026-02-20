package com.easypay.provider.entity.account;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_account_balance")
public class AccountBalance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_no", length = 30, nullable = false, unique = true)
    private String accountNo;

    @Column(name = "account_name", length = 60)
    private String accountName;

    @Column(name = "account_type")
    private Byte accountType;

    @Column(name = "balance")
    private Long balance = 0L;

    @Column(name = "frozen_amount")
    private Long frozenAmount = 0L;

    @Column(name = "unsettled_amount")
    private Long unsettledAmount = 0L;

    @Column(name = "settled_amount")
    private Long settledAmount = 0L;

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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Byte getAccountType() {
        return accountType;
    }

    public void setAccountType(Byte accountType) {
        this.accountType = accountType;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Long frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public Long getUnsettledAmount() {
        return unsettledAmount;
    }

    public void setUnsettledAmount(Long unsettledAmount) {
        this.unsettledAmount = unsettledAmount;
    }

    public Long getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(Long settledAmount) {
        this.settledAmount = settledAmount;
    }
}
