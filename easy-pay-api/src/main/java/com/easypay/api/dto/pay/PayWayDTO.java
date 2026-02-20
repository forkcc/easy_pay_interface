package com.easypay.api.dto.pay;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付方式数据传输对象
 */
public class PayWayDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 支付方式编码
    private String wayCode;
    // 支付方式名称
    private String wayName;
    // 状态
    private Byte state;
    // 备注
    private String remark;
    // 创建时间
    private LocalDateTime createdAt;

    public PayWayDTO() {
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
