package com.easypay.provider.entity.pay;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 支付接口定义实体，对应表 t_pay_interface_define。
 */
@Entity
@Table(name = "t_pay_interface_define")
public class PayInterfaceDefine extends BaseEntity {

    @Id
    @Column(name = "if_code", length = 20)
    private String ifCode;

    @Column(name = "if_name", length = 60)
    private String ifName;

    @Column(name = "if_type")
    private Byte ifType;

    @Column(name = "config_page_type", length = 20)
    private String configPageType;

    @Column(name = "icon", length = 128)
    private String icon;

    @Column(name = "bg_color", length = 20)
    private String bgColor;

    @Column(name = "state")
    private Byte state;

    @Column(name = "remark", length = 256)
    private String remark;

    @Column(name = "config_info", columnDefinition = "TEXT")
    private String configInfo;

    public String getIfCode() {
        return ifCode;
    }

    public void setIfCode(String ifCode) {
        this.ifCode = ifCode;
    }

    public String getIfName() {
        return ifName;
    }

    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    public Byte getIfType() {
        return ifType;
    }

    public void setIfType(Byte ifType) {
        this.ifType = ifType;
    }

    public String getConfigPageType() {
        return configPageType;
    }

    public void setConfigPageType(String configPageType) {
        this.configPageType = configPageType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
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

    public String getConfigInfo() {
        return configInfo;
    }

    public void setConfigInfo(String configInfo) {
        this.configInfo = configInfo;
    }
}
