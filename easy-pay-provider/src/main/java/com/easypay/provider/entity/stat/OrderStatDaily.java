package com.easypay.provider.entity.stat;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "t_order_stat_daily")
public class OrderStatDaily extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate;

    @Column(name = "mch_no", length = 30)
    private String mchNo;

    @Column(name = "agent_no", length = 30)
    private String agentNo;

    @Column(name = "way_code", length = 20)
    private String wayCode;

    @Column(name = "passage_id")
    private Long passageId;

    @Column(name = "order_count")
    private Integer orderCount = 0;

    @Column(name = "order_amount")
    private Long orderAmount = 0L;

    @Column(name = "fee_amount")
    private Long feeAmount = 0L;

    @Column(name = "success_count")
    private Integer successCount = 0;

    @Column(name = "success_amount")
    private Long successAmount = 0L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStatDate() {
        return statDate;
    }

    public void setStatDate(LocalDate statDate) {
        this.statDate = statDate;
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

    public String getWayCode() {
        return wayCode;
    }

    public void setWayCode(String wayCode) {
        this.wayCode = wayCode;
    }

    public Long getPassageId() {
        return passageId;
    }

    public void setPassageId(Long passageId) {
        this.passageId = passageId;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Long getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Long feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Long getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(Long successAmount) {
        this.successAmount = successAmount;
    }
}
