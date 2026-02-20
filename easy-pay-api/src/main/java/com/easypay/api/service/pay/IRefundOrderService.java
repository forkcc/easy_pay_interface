package com.easypay.api.service.pay;

import com.easypay.api.dto.pay.RefundOrderDTO;
import com.easypay.api.result.PageResult;

public interface IRefundOrderService {

    RefundOrderDTO create(RefundOrderDTO dto);

    RefundOrderDTO getById(String refundOrderId);

    RefundOrderDTO getByMchRefundNo(String mchNo, String mchRefundNo);

    boolean updateState(String refundOrderId, Byte state, String channelRefundNo);

    PageResult<RefundOrderDTO> page(RefundOrderDTO query, int pageNum, int pageSize);
}
