package com.easypay.api.service.pay;

import com.easypay.api.dto.pay.TransferOrderDTO;
import com.easypay.api.result.PageResult;

public interface ITransferOrderService {

    TransferOrderDTO create(TransferOrderDTO dto);

    TransferOrderDTO getById(String transferId);

    TransferOrderDTO getByMchTransferNo(String mchNo, String mchTransferNo);

    boolean updateState(String transferId, Byte state, String channelOrderNo);

    PageResult<TransferOrderDTO> page(TransferOrderDTO query, int pageNum, int pageSize);
}
