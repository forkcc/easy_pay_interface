package com.easypay.provider.entity.agent;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_agent_account")
public class AgentAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "agent_no", length = 30, nullable = false, unique = true)
    private String agentNo;

    @Column(name = "balance")
    private Long balance = 0L;

    @Column(name = "frozen_amount")
    private Long frozenAmount = 0L;

    @Column(name = "settled_amount")
    private Long settledAmount = 0L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
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

    public Long getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(Long settledAmount) {
        this.settledAmount = settledAmount;
    }
}
