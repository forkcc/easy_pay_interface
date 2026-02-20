package com.easypay.provider.entity.pay;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_mch_pay_passage")
public class MchPayPassage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "mch_no", length = 30, nullable = false)
    private String mchNo;

    @Column(name = "app_id", length = 30, nullable = false)
    private String appId;

    @Column(name = "way_code", length = 20, nullable = false)
    private String wayCode;

    @Column(name = "passage_id")
    private Long passageId;

    @Column(name = "fee_rate")
    private Long feeRate;

    @Column(name = "fee_amount")
    private Long feeAmount;

    @Column(name = "state")
    private Byte state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
