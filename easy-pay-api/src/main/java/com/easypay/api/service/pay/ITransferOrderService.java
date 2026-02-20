package com.easypay.api.service.pay;

import com.easypay.api.dto.pay.TransferOrderDTO;
import com.easypay.api.result.PageResult;

/**
 * 转账订单服务接口
 */
public interface ITransferOrderService {

    // 创建转账订单
    TransferOrderDTO create(TransferOrderDTO dto);

    // 根据转账订单号查询转账订单
    TransferOrderDTO getById(String transferId);

    // 根据商户转账单号查询转账订单
    TransferOrderDTO getByMchTransferNo(String mchNo, String mchTransferNo);

    // 更新转账订单状态
    boolean updateState(String transferId, Byte state, String channelOrderNo);

    // 分页查询转账订单
    PageResult<TransferOrderDTO> page(TransferOrderDTO query, int pageNum, int pageSize);
}
