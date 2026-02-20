package com.easypay.provider.converter;

import com.easypay.api.dto.agent.AgentAccountDTO;
import com.easypay.api.dto.agent.AgentInfoDTO;
import com.easypay.provider.entity.agent.AgentAccount;
import com.easypay.provider.entity.agent.AgentInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AgentConverter {

    public static AgentInfoDTO toDTO(AgentInfo entity) {
        if (entity == null) {
            return null;
        }
        AgentInfoDTO dto = new AgentInfoDTO();
        dto.setAgentNo(entity.getAgentNo());
        dto.setAgentName(entity.getAgentName());
        dto.setLevel(entity.getLevel());
        dto.setParentAgentNo(entity.getParentAgentNo());
        dto.setProfitRate(entity.getProfitRate());
        dto.setState(entity.getState());
        dto.setContactName(entity.getContactName());
        dto.setContactTel(entity.getContactTel());
        dto.setContactEmail(entity.getContactEmail());
        dto.setRemark(entity.getRemark());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static AgentInfo toEntity(AgentInfoDTO dto) {
        if (dto == null) {
            return null;
        }
        AgentInfo entity = new AgentInfo();
        entity.setAgentNo(dto.getAgentNo());
        entity.setAgentName(dto.getAgentName());
        entity.setLevel(dto.getLevel());
        entity.setParentAgentNo(dto.getParentAgentNo());
        entity.setProfitRate(dto.getProfitRate());
        entity.setState(dto.getState());
        entity.setContactName(dto.getContactName());
        entity.setContactTel(dto.getContactTel());
        entity.setContactEmail(dto.getContactEmail());
        entity.setRemark(dto.getRemark());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<AgentInfoDTO> toAgentInfoDTOList(List<AgentInfo> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<AgentInfoDTO> result = new ArrayList<>(entities.size());
        for (AgentInfo entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static AgentAccountDTO toDTO(AgentAccount entity) {
        if (entity == null) {
            return null;
        }
        AgentAccountDTO dto = new AgentAccountDTO();
        dto.setId(entity.getId());
        dto.setAgentNo(entity.getAgentNo());
        dto.setBalance(entity.getBalance());
        dto.setFrozenAmount(entity.getFrozenAmount());
        dto.setSettledAmount(entity.getSettledAmount());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static AgentAccount toEntity(AgentAccountDTO dto) {
        if (dto == null) {
            return null;
        }
        AgentAccount entity = new AgentAccount();
        entity.setId(dto.getId());
        entity.setAgentNo(dto.getAgentNo());
        entity.setBalance(dto.getBalance());
        entity.setFrozenAmount(dto.getFrozenAmount());
        entity.setSettledAmount(dto.getSettledAmount());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<AgentAccountDTO> toAgentAccountDTOList(List<AgentAccount> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<AgentAccountDTO> result = new ArrayList<>(entities.size());
        for (AgentAccount entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }
}
