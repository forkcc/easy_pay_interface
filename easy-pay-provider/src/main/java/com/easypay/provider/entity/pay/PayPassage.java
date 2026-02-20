package com.easypay.provider.entity.pay;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_pay_passage")
public class PayPassage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passage_id")
    private Long passageId;

    @Column(name = "passage_name", length = 60)
    private String passageName;

    @Column(name = "if_code", length = 20)
    private String ifCode;

    @Column(name = "way_code", length = 20)
    private String wayCode;

    @Column(name = "fee_rate")
    private Long feeRate;

    @Column(name = "fee_amount")
    private Long feeAmount;

    @Column(name = "state")
    private Byte state;

    @Column(name = "remark", length = 256)
    private String remark;

    public Long getPassageId() {
        return passageId;
    }

    public void setPassageId(Long passageId) {
        this.passageId = passageId;
    }

    public String getPassageName() {
        return passageName;
    }

    public void setPassageName(String passageName) {
        this.passageName = passageName;
    }

    public String getIfCode() {
        return ifCode;
    }

    public void setIfCode(String ifCode) {
        this.ifCode = ifCode;
    }

    public String getWayCode() {
        return wayCode;
    }

    public void setWayCode(String wayCode) {
        this.wayCode = wayCode;
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

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
