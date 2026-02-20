package com.easypay.provider.service.impl.pay;

import com.easypay.api.dto.pay.TransferOrderDTO;
import com.easypay.api.result.PageResult;
import com.easypay.api.service.pay.ITransferOrderService;
import com.easypay.provider.converter.PayConverter;
import com.easypay.provider.entity.pay.TransferOrder;
import com.easypay.provider.repository.pay.TransferOrderRepository;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 转账订单服务实现，实现 {@link ITransferOrderService} 接口，提供转账订单的创建、状态更新及分页查询功能
 */
@DubboService
public class TransferOrderServiceImpl implements ITransferOrderService {

    @Resource
    private TransferOrderRepository transferOrderRepository;

    @Override
    @Transactional
    public TransferOrderDTO create(TransferOrderDTO dto) {
        TransferOrder entity = PayConverter.toEntity(dto);
        entity = transferOrderRepository.save(entity);
        return PayConverter.toDTO(entity);
    }

    @Override
    public TransferOrderDTO getById(String transferId) {
        return transferOrderRepository.findById(transferId).map(PayConverter::toDTO).orElse(null);
    }

    @Override
    public TransferOrderDTO getByMchTransferNo(String mchNo, String mchTransferNo) {
        return transferOrderRepository.findByMchNoAndMchTransferNo(mchNo, mchTransferNo)
                .map(PayConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean updateState(String transferId, Byte state, String channelOrderNo) {
        return transferOrderRepository.findById(transferId).map(entity -> {
            entity.setState(state);
            entity.setChannelOrderNo(channelOrderNo);
            if (state != null && state == 2) {
                entity.setSuccessTime(LocalDateTime.now());
            }
            transferOrderRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    public PageResult<TransferOrderDTO> page(TransferOrderDTO query, int pageNum, int pageSize) {
        TransferOrder probe = new TransferOrder();
        probe.setMchNo(query.getMchNo());
        probe.setAppId(query.getAppId());
        probe.setState(query.getState());
        probe.setWayCode(query.getWayCode());
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<TransferOrder> example = Example.of(probe, matcher);
        Page<TransferOrder> page = transferOrderRepository.findAll(example, PageRequest.of(pageNum - 1, pageSize));
        List<TransferOrderDTO> dtoList = PayConverter.toTransferOrderDTOList(page.getContent());
        return new PageResult<>(dtoList, page.getTotalElements(), pageNum, pageSize);
    }
}
