package com.easypay.provider.service.impl.pay;

import com.easypay.api.dto.pay.RefundOrderDTO;
import com.easypay.api.result.PageResult;
import com.easypay.api.service.pay.IRefundOrderService;
import com.easypay.provider.converter.PayConverter;
import com.easypay.provider.entity.pay.RefundOrder;
import com.easypay.provider.repository.pay.RefundOrderRepository;
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
 * 退款订单服务实现，实现 {@link IRefundOrderService} 接口，提供退款订单的创建、状态更新及分页查询功能
 */
@DubboService
public class RefundOrderServiceImpl implements IRefundOrderService {

    @Resource
    private RefundOrderRepository refundOrderRepository;

    @Override
    @Transactional
    public RefundOrderDTO create(RefundOrderDTO dto) {
        RefundOrder entity = PayConverter.toEntity(dto);
        entity = refundOrderRepository.save(entity);
        return PayConverter.toDTO(entity);
    }

    @Override
    public RefundOrderDTO getById(String refundOrderId) {
        return refundOrderRepository.findById(refundOrderId).map(PayConverter::toDTO).orElse(null);
    }

    @Override
    public RefundOrderDTO getByMchRefundNo(String mchNo, String mchRefundNo) {
        return refundOrderRepository.findByMchNoAndMchRefundNo(mchNo, mchRefundNo)
                .map(PayConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean updateState(String refundOrderId, Byte state, String channelRefundNo) {
        return refundOrderRepository.findById(refundOrderId).map(entity -> {
            entity.setState(state);
            entity.setChannelRefundNo(channelRefundNo);
            if (state != null && state == 2) {
                entity.setSuccessTime(LocalDateTime.now());
            }
            refundOrderRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    public PageResult<RefundOrderDTO> page(RefundOrderDTO query, int pageNum, int pageSize) {
        RefundOrder probe = new RefundOrder();
        probe.setMchNo(query.getMchNo());
        probe.setAppId(query.getAppId());
        probe.setState(query.getState());
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<RefundOrder> example = Example.of(probe, matcher);
        Page<RefundOrder> page = refundOrderRepository.findAll(example, PageRequest.of(pageNum - 1, pageSize));
        List<RefundOrderDTO> dtoList = PayConverter.toRefundOrderDTOList(page.getContent());
        return new PageResult<>(dtoList, page.getTotalElements(), pageNum, pageSize);
    }
}
