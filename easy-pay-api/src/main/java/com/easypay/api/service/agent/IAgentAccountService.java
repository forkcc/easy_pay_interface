package com.easypay.api.service.agent;

import com.easypay.api.dto.agent.AgentAccountDTO;

public interface IAgentAccountService {

    AgentAccountDTO getByAgentNo(String agentNo);

    boolean initAccount(String agentNo);

    boolean changeBalance(String agentNo, Long changeAmount, String bizOrderNo, String remark);

    boolean freeze(String agentNo, Long amount, String bizOrderNo);

    boolean unfreeze(String agentNo, Long amount, String bizOrderNo);
}
