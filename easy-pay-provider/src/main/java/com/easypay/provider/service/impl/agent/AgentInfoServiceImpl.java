package com.easypay.provider.service.impl.agent;

import com.easypay.api.dto.agent.AgentInfoDTO;
import com.easypay.api.result.PageResult;
import com.easypay.api.service.agent.IAgentInfoService;
import com.easypay.provider.converter.AgentConverter;
import com.easypay.provider.entity.agent.AgentInfo;
import com.easypay.provider.repository.agent.AgentInfoRepository;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 代理商信息服务实现，实现 {@link IAgentInfoService} 接口，提供代理商的增删改查及分页功能
 */
@DubboService
public class AgentInfoServiceImpl implements IAgentInfoService {

    @Resource
    private AgentInfoRepository agentInfoRepository;

    @Override
    @Transactional
    public AgentInfoDTO create(AgentInfoDTO dto) {
        AgentInfo entity = AgentConverter.toEntity(dto);
        entity = agentInfoRepository.save(entity);
        return AgentConverter.toDTO(entity);
    }

    @Override
    public AgentInfoDTO getByAgentNo(String agentNo) {
        return agentInfoRepository.findById(agentNo).map(AgentConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean update(AgentInfoDTO dto) {
        return agentInfoRepository.findById(dto.getAgentNo()).map(entity -> {
            entity.setAgentName(dto.getAgentName());
            entity.setLevel(dto.getLevel());
            entity.setParentAgentNo(dto.getParentAgentNo());
            entity.setProfitRate(dto.getProfitRate());
            entity.setContactName(dto.getContactName());
            entity.setContactTel(dto.getContactTel());
            entity.setContactEmail(dto.getContactEmail());
            entity.setRemark(dto.getRemark());
            agentInfoRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    @Transactional
    public boolean updateState(String agentNo, Byte state) {
        return agentInfoRepository.findById(agentNo).map(entity -> {
            entity.setState(state);
            agentInfoRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    public PageResult<AgentInfoDTO> page(AgentInfoDTO query, int pageNum, int pageSize) {
        AgentInfo probe = new AgentInfo();
        probe.setAgentNo(query.getAgentNo());
        probe.setAgentName(query.getAgentName());
        probe.setState(query.getState());
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("agentNo", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("agentName", ExampleMatcher.GenericPropertyMatcher::contains)
                .withMatcher("state", ExampleMatcher.GenericPropertyMatcher::exact);
        Example<AgentInfo> example = Example.of(probe, matcher);
        Page<AgentInfo> page = agentInfoRepository.findAll(example, PageRequest.of(pageNum - 1, pageSize));
        List<AgentInfoDTO> dtoList = AgentConverter.toAgentInfoDTOList(page.getContent());
        return new PageResult<>(dtoList, page.getTotalElements(), pageNum, pageSize);
    }

    @Override
    public List<AgentInfoDTO> listByParentAgentNo(String parentAgentNo) {
        return AgentConverter.toAgentInfoDTOList(agentInfoRepository.findByParentAgentNo(parentAgentNo));
    }
}
