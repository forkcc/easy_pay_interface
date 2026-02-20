package com.easypay.provider.entity.pay;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 支付接口配置实体，对应表 t_pay_interface_config。
 */
@Entity
@Table(name = "t_pay_interface_config")
public class PayInterfaceConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "info_type", length = 20, nullable = false)
    private String infoType;

    @Column(name = "info_id", length = 64, nullable = false)
    private String infoId;

    @Column(name = "if_code", length = 20, nullable = false)
    private String ifCode;

    @Column(name = "if_params", columnDefinition = "TEXT")
    private String ifParams;

    @Column(name = "state")
    private Byte state;

    @Column(name = "remark", length = 256)
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getIfCode() {
        return ifCode;
    }

    public void setIfCode(String ifCode) {
        this.ifCode = ifCode;
    }

    public String getIfParams() {
        return ifParams;
    }

    public void setIfParams(String ifParams) {
        this.ifParams = ifParams;
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
