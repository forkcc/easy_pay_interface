package com.easypay.api.service.pay;

import com.easypay.api.dto.pay.PayOrderDTO;
import com.easypay.api.result.PageResult;

import java.time.LocalDateTime;

public interface IPayOrderService {

    PayOrderDTO create(PayOrderDTO dto);

    PayOrderDTO getById(String payOrderId);

    PayOrderDTO getByMchOrderNo(String mchNo, String mchOrderNo);

    boolean updateState(String payOrderId, Byte currentState, Byte targetState, String channelOrderNo);

    boolean updateNotifySent(String payOrderId);

    PageResult<PayOrderDTO> page(PayOrderDTO query, int pageNum, int pageSize);

    Long sumSuccessAmount(String mchNo, LocalDateTime startTime, LocalDateTime endTime);
}
