package com.easypay.provider.service.impl.sys;

import com.easypay.api.dto.sys.SysConfigDTO;
import com.easypay.api.dto.sys.SysLogDTO;
import com.easypay.api.result.PageResult;
import com.easypay.api.service.sys.ISysConfigService;
import com.easypay.provider.converter.SysConverter;
import com.easypay.provider.entity.sys.SysConfig;
import com.easypay.provider.entity.sys.SysLog;
import com.easypay.provider.repository.sys.SysConfigRepository;
import com.easypay.provider.repository.sys.SysLogRepository;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DubboService
public class SysConfigServiceImpl implements ISysConfigService {

    @Resource
    private SysConfigRepository sysConfigRepository;

    @Resource
    private SysLogRepository sysLogRepository;

    @Override
    public SysConfigDTO getByKey(String configKey) {
        return sysConfigRepository.findById(configKey).map(SysConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean save(SysConfigDTO dto) {
        sysConfigRepository.save(SysConverter.toEntity(dto));
        return true;
    }

    @Override
    @Transactional
    public boolean deleteByKey(String configKey) {
        sysConfigRepository.deleteById(configKey);
        return true;
    }

    @Override
    public List<SysConfigDTO> listByGroup(String configGroup) {
        return SysConverter.toSysConfigDTOList(sysConfigRepository.findByConfigGroup(configGroup));
    }

    @Override
    @Transactional
    public void createLog(SysLogDTO dto) {
        SysLog entity = SysConverter.toEntity(dto);
        sysLogRepository.save(entity);
    }

    @Override
    public PageResult<SysLogDTO> pageLog(String sysType, String userId, int pageNum, int pageSize) {
        Page<SysLog> page;
        if (userId != null) {
            page = sysLogRepository.findBySysTypeAndUserId(sysType, userId, PageRequest.of(pageNum - 1, pageSize));
        } else {
            page = sysLogRepository.findBySysType(sysType, PageRequest.of(pageNum - 1, pageSize));
        }
        List<SysLogDTO> dtoList = SysConverter.toSysLogDTOList(page.getContent());
        return new PageResult<>(dtoList, page.getTotalElements(), pageNum, pageSize);
    }
}
