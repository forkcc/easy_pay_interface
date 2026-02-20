package com.easypay.api.service.agent;

import com.easypay.api.dto.agent.AgentAccountDTO;

/**
 * 代理商账户服务接口
 */
public interface IAgentAccountService {

    // 根据代理商编号查询账户信息
    AgentAccountDTO getByAgentNo(String agentNo);

    // 初始化代理商账户
    boolean initAccount(String agentNo);

    // 变更代理商账户余额
    boolean changeBalance(String agentNo, Long changeAmount, String bizOrderNo, String remark);

    // 冻结代理商账户资金
    boolean freeze(String agentNo, Long amount, String bizOrderNo);

    // 解冻代理商账户资金
    boolean unfreeze(String agentNo, Long amount, String bizOrderNo);
}
