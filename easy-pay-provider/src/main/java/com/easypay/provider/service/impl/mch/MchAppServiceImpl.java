package com.easypay.provider.service.impl.mch;

import com.easypay.api.dto.mch.MchAppDTO;
import com.easypay.api.result.PageResult;
import com.easypay.api.service.mch.IMchAppService;
import com.easypay.provider.converter.MchConverter;
import com.easypay.provider.entity.mch.MchApp;
import com.easypay.provider.repository.mch.MchAppRepository;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@DubboService
public class MchAppServiceImpl implements IMchAppService {

    @Resource
    private MchAppRepository mchAppRepository;

    @Override
    @Transactional
    public MchAppDTO create(MchAppDTO dto) {
        MchApp entity = MchConverter.toEntity(dto);
        entity = mchAppRepository.save(entity);
        return MchConverter.toDTO(entity);
    }

    @Override
    public MchAppDTO getByAppId(String appId) {
        return mchAppRepository.findById(appId).map(MchConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean update(MchAppDTO dto) {
        return mchAppRepository.findById(dto.getAppId()).map(entity -> {
            entity.setAppName(dto.getAppName());
            entity.setState(dto.getState());
            entity.setNotifyUrl(dto.getNotifyUrl());
            entity.setReturnUrl(dto.getReturnUrl());
            entity.setRemark(dto.getRemark());
            mchAppRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    @Transactional
    public boolean delete(String appId) {
        mchAppRepository.deleteById(appId);
        return true;
    }

    @Override
    @Transactional
    public String resetAppSecret(String appId) {
        return mchAppRepository.findById(appId).map(entity -> {
            String secret = UUID.randomUUID().toString().replace("-", "");
            entity.setAppSecret(secret);
            mchAppRepository.save(entity);
            return secret;
        }).orElse(null);
    }

    @Override
    public List<MchAppDTO> listByMchNo(String mchNo) {
        return MchConverter.toMchAppDTOList(mchAppRepository.findByMchNo(mchNo));
    }

    @Override
    public PageResult<MchAppDTO> page(String mchNo, int pageNum, int pageSize) {
        Page<MchApp> page = mchAppRepository.findByMchNo(mchNo, PageRequest.of(pageNum - 1, pageSize));
        List<MchAppDTO> dtoList = MchConverter.toMchAppDTOList(page.getContent());
        return new PageResult<>(dtoList, page.getTotalElements(), pageNum, pageSize);
    }
}
