package com.easypay.provider.service.impl.agent;

import com.easypay.api.dto.agent.AgentAccountDTO;
import com.easypay.api.service.agent.IAgentAccountService;
import com.easypay.provider.converter.AgentConverter;
import com.easypay.provider.entity.agent.AgentAccount;
import com.easypay.provider.repository.agent.AgentAccountRepository;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

@DubboService
public class AgentAccountServiceImpl implements IAgentAccountService {

    @Resource
    private AgentAccountRepository agentAccountRepository;

    @Override
    public AgentAccountDTO getByAgentNo(String agentNo) {
        return agentAccountRepository.findByAgentNo(agentNo).map(AgentConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean initAccount(String agentNo) {
        AgentAccount entity = new AgentAccount();
        entity.setAgentNo(agentNo);
        entity.setBalance(0L);
        entity.setFrozenAmount(0L);
        entity.setSettledAmount(0L);
        agentAccountRepository.save(entity);
        return true;
    }

    @Override
    @Transactional
    public boolean changeBalance(String agentNo, Long changeAmount, String bizOrderNo, String remark) {
        return agentAccountRepository.findByAgentNo(agentNo).map(entity -> {
            entity.setBalance(entity.getBalance() + changeAmount);
            agentAccountRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    @Transactional
    public boolean freeze(String agentNo, Long amount, String bizOrderNo) {
        return agentAccountRepository.findByAgentNo(agentNo).map(entity -> {
            entity.setBalance(entity.getBalance() - amount);
            entity.setFrozenAmount(entity.getFrozenAmount() + amount);
            agentAccountRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    @Transactional
    public boolean unfreeze(String agentNo, Long amount, String bizOrderNo) {
        return agentAccountRepository.findByAgentNo(agentNo).map(entity -> {
            entity.setBalance(entity.getBalance() + amount);
            entity.setFrozenAmount(entity.getFrozenAmount() - amount);
            agentAccountRepository.save(entity);
            return true;
        }).orElse(false);
    }
}
