package com.easypay.api.service.pay;

import com.easypay.api.dto.pay.PayOrderDTO;
import com.easypay.api.result.PageResult;

import java.time.LocalDateTime;

/**
 * 支付订单服务接口
 */
public interface IPayOrderService {

    // 创建支付订单
    PayOrderDTO create(PayOrderDTO dto);

    // 根据支付订单号查询订单
    PayOrderDTO getById(String payOrderId);

    // 根据商户订单号查询订单
    PayOrderDTO getByMchOrderNo(String mchNo, String mchOrderNo);

    // 更新订单状态
    boolean updateState(String payOrderId, Byte currentState, Byte targetState, String channelOrderNo);

    // 标记订单通知已发送
    boolean updateNotifySent(String payOrderId);

    // 分页查询支付订单
    PageResult<PayOrderDTO> page(PayOrderDTO query, int pageNum, int pageSize);

    // 统计指定时间范围内的成功交易金额
    Long sumSuccessAmount(String mchNo, LocalDateTime startTime, LocalDateTime endTime);
}
