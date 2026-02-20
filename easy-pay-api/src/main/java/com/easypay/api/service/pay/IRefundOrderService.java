package com.easypay.api.service.pay;

import com.easypay.api.dto.pay.RefundOrderDTO;
import com.easypay.api.result.PageResult;

/**
 * 退款订单服务接口
 */
public interface IRefundOrderService {

    // 创建退款订单
    RefundOrderDTO create(RefundOrderDTO dto);

    // 根据退款订单号查询退款订单
    RefundOrderDTO getById(String refundOrderId);

    // 根据商户退款单号查询退款订单
    RefundOrderDTO getByMchRefundNo(String mchNo, String mchRefundNo);

    // 更新退款订单状态
    boolean updateState(String refundOrderId, Byte state, String channelRefundNo);

    // 分页查询退款订单
    PageResult<RefundOrderDTO> page(RefundOrderDTO query, int pageNum, int pageSize);
}
