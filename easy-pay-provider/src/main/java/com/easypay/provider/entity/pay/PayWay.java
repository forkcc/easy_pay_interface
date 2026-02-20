package com.easypay.provider.entity.pay;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 支付方式实体，对应表 t_pay_way。
 */
@Entity
@Table(name = "t_pay_way")
public class PayWay extends BaseEntity {

    @Id
    @Column(name = "way_code", length = 20)
    private String wayCode;

    @Column(name = "way_name", length = 30)
    private String wayName;

    @Column(name = "state")
    private Byte state;

    @Column(name = "remark", length = 128)
    private String remark;

    public String getWayCode() {
        return wayCode;
    }

    public void setWayCode(String wayCode) {
        this.wayCode = wayCode;
    }

    public String getWayName() {
        return wayName;
    }

    public void setWayName(String wayName) {
        this.wayName = wayName;
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
