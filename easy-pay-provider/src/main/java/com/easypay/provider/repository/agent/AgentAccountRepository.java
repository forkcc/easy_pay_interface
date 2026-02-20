package com.easypay.provider.repository.agent;

import com.easypay.provider.entity.agent.AgentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 代理商账户数据访问接口，对应实体 {@link AgentAccount}。
 */
public interface AgentAccountRepository extends JpaRepository<AgentAccount, Long> {

    Optional<AgentAccount> findByAgentNo(String agentNo);
}
