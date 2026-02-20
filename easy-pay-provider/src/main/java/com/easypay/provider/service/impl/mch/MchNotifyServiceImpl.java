package com.easypay.provider.service.impl.mch;

import com.easypay.api.dto.mch.MchNotifyDTO;
import com.easypay.api.result.PageResult;
import com.easypay.api.service.mch.IMchNotifyService;
import com.easypay.provider.converter.MchConverter;
import com.easypay.provider.entity.mch.MchNotify;
import com.easypay.provider.repository.mch.MchNotifyRepository;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商户通知服务实现，实现 {@link IMchNotifyService} 接口，提供商户回调通知的创建、状态更新及待通知查询功能
 */
@DubboService
public class MchNotifyServiceImpl implements IMchNotifyService {

    @Resource
    private MchNotifyRepository mchNotifyRepository;

    private static final Byte STATE_INIT = 0;

    @Override
    @Transactional
    public MchNotifyDTO create(MchNotifyDTO dto) {
        MchNotify entity = MchConverter.toEntity(dto);
        entity = mchNotifyRepository.save(entity);
        return MchConverter.toDTO(entity);
    }

    @Override
    public MchNotifyDTO getById(Long notifyId) {
        return mchNotifyRepository.findById(notifyId).map(MchConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean updateState(Long notifyId, Byte state, Integer notifyCount) {
        return mchNotifyRepository.findById(notifyId).map(entity -> {
            entity.setState(state);
            entity.setNotifyCount(notifyCount);
            entity.setLastNotifyTime(LocalDateTime.now());
            mchNotifyRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    public List<MchNotifyDTO> listPendingNotify(int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        List<MchNotify> list = mchNotifyRepository.findByStateOrderByCreatedAtAsc(STATE_INIT, pageable);
        return MchConverter.toMchNotifyDTOList(list);
    }

    @Override
    public PageResult<MchNotifyDTO> page(String mchNo, String orderId, int pageNum, int pageSize) {
        Page<MchNotify> page;
        if (orderId != null && !orderId.isEmpty()) {
            page = mchNotifyRepository.findByMchNoAndOrderId(mchNo, orderId, PageRequest.of(pageNum - 1, pageSize));
        } else {
            page = mchNotifyRepository.findByMchNo(mchNo, PageRequest.of(pageNum - 1, pageSize));
        }
        List<MchNotifyDTO> dtoList = MchConverter.toMchNotifyDTOList(page.getContent());
        return new PageResult<>(dtoList, page.getTotalElements(), pageNum, pageSize);
    }
}
