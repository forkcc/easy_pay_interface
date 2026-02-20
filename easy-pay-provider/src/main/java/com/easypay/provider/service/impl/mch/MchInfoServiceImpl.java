package com.easypay.provider.service.impl.mch;

import com.easypay.api.dto.mch.MchInfoDTO;
import com.easypay.api.result.PageResult;
import com.easypay.api.service.mch.IMchInfoService;
import com.easypay.provider.converter.MchConverter;
import com.easypay.provider.entity.mch.MchInfo;
import com.easypay.provider.repository.mch.MchInfoRepository;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DubboService
public class MchInfoServiceImpl implements IMchInfoService {

    @Resource
    private MchInfoRepository mchInfoRepository;

    @Override
    @Transactional
    public MchInfoDTO create(MchInfoDTO dto) {
        MchInfo entity = MchConverter.toEntity(dto);
        entity = mchInfoRepository.save(entity);
        return MchConverter.toDTO(entity);
    }

    @Override
    public MchInfoDTO getByMchNo(String mchNo) {
        return mchInfoRepository.findById(mchNo).map(MchConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean update(MchInfoDTO dto) {
        return mchInfoRepository.findById(dto.getMchNo()).map(entity -> {
            entity.setMchName(dto.getMchName());
            entity.setMchShortName(dto.getMchShortName());
            entity.setType(dto.getType());
            entity.setContactName(dto.getContactName());
            entity.setContactTel(dto.getContactTel());
            entity.setContactEmail(dto.getContactEmail());
            entity.setRemark(dto.getRemark());
            mchInfoRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    @Transactional
    public boolean updateState(String mchNo, Byte state) {
        return mchInfoRepository.findById(mchNo).map(entity -> {
            entity.setState(state);
            mchInfoRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    public PageResult<MchInfoDTO> page(MchInfoDTO query, int pageNum, int pageSize) {
        MchInfo probe = new MchInfo();
        probe.setMchNo(query.getMchNo());
        probe.setMchName(query.getMchName());
        probe.setState(query.getState());
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("mchNo", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("mchName", ExampleMatcher.GenericPropertyMatcher::contains)
                .withMatcher("state", ExampleMatcher.GenericPropertyMatcher::exact);
        Example<MchInfo> example = Example.of(probe, matcher);
        Page<MchInfo> page = mchInfoRepository.findAll(example, PageRequest.of(pageNum - 1, pageSize));
        List<MchInfoDTO> dtoList = MchConverter.toMchInfoDTOList(page.getContent());
        return new PageResult<>(dtoList, page.getTotalElements(), pageNum, pageSize);
    }

    @Override
    public PageResult<MchInfoDTO> pageByAgentNo(String agentNo, int pageNum, int pageSize) {
        Page<MchInfo> page = mchInfoRepository.findByAgentNo(agentNo, PageRequest.of(pageNum - 1, pageSize));
        List<MchInfoDTO> dtoList = MchConverter.toMchInfoDTOList(page.getContent());
        return new PageResult<>(dtoList, page.getTotalElements(), pageNum, pageSize);
    }
}
