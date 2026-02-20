package com.easypay.provider.service.impl.pay;

import com.easypay.api.dto.pay.PayOrderDTO;
import com.easypay.api.result.PageResult;
import com.easypay.api.service.pay.IPayOrderService;
import com.easypay.provider.converter.PayConverter;
import com.easypay.provider.entity.pay.PayOrder;
import com.easypay.provider.repository.pay.PayOrderRepository;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DubboService
public class PayOrderServiceImpl implements IPayOrderService {

    @Resource
    private PayOrderRepository payOrderRepository;

    private static final Byte STATE_SUCCESS = 2;

    @Override
    @Transactional
    public PayOrderDTO create(PayOrderDTO dto) {
        PayOrder entity = PayConverter.toEntity(dto);
        entity = payOrderRepository.save(entity);
        return PayConverter.toDTO(entity);
    }

    @Override
    public PayOrderDTO getById(String payOrderId) {
        return payOrderRepository.findById(payOrderId).map(PayConverter::toDTO).orElse(null);
    }

    @Override
    public PayOrderDTO getByMchOrderNo(String mchNo, String mchOrderNo) {
        return payOrderRepository.findByMchNoAndMchOrderNo(mchNo, mchOrderNo)
                .map(PayConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean updateState(String payOrderId, Byte currentState, Byte targetState, String channelOrderNo) {
        Optional<PayOrder> opt = payOrderRepository.findById(payOrderId);
        if (opt.isEmpty()) {
            return false;
        }
        PayOrder entity = opt.get();
        if (!currentState.equals(entity.getState())) {
            return false;
        }
        entity.setState(targetState);
        entity.setChannelOrderNo(channelOrderNo);
        if (STATE_SUCCESS.equals(targetState)) {
            entity.setSuccessTime(LocalDateTime.now());
        }
        payOrderRepository.save(entity);
        return true;
    }

    @Override
    @Transactional
    public boolean updateNotifySent(String payOrderId) {
        return payOrderRepository.findById(payOrderId).isPresent();
    }

    @Override
    public PageResult<PayOrderDTO> page(PayOrderDTO query, int pageNum, int pageSize) {
        PayOrder probe = new PayOrder();
        probe.setMchNo(query.getMchNo());
        probe.setAppId(query.getAppId());
        probe.setState(query.getState());
        probe.setWayCode(query.getWayCode());
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<PayOrder> example = Example.of(probe, matcher);
        Page<PayOrder> page = payOrderRepository.findAll(example, PageRequest.of(pageNum - 1, pageSize));
        List<PayOrderDTO> dtoList = PayConverter.toPayOrderDTOList(page.getContent());
        return new PageResult<>(dtoList, page.getTotalElements(), pageNum, pageSize);
    }

    @Override
    public Long sumSuccessAmount(String mchNo, LocalDateTime startTime, LocalDateTime endTime) {
        Long sum = payOrderRepository.sumSuccessAmount(mchNo, startTime, endTime);
        return sum != null ? sum : 0L;
    }
}
