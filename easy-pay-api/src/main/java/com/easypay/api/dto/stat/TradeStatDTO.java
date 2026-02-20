package com.easypay.api.dto.stat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 交易汇总统计数据传输对象
 */
public class TradeStatDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 统计开始日期
    private LocalDate startDate;
    // 统计结束日期
    private LocalDate endDate;
    // 商户编号
    private String mchNo;
    // 代理商编号
    private String agentNo;
    // 支付方式编码
    private String wayCode;
    // 通道ID
    private Long passageId;
    // 总订单数
    private Integer totalCount;
    // 总金额
    private Long totalAmount;
    // 总手续费
    private Long totalFee;
    // 成功笔数
    private Integer successCount;
    // 成功金额
    private Long successAmount;

    public TradeStatDTO() {
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
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
