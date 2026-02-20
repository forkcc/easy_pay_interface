package com.easypay.provider.repository.agent;

import com.easypay.provider.entity.agent.AgentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentAccountRepository extends JpaRepository<AgentAccount, Long> {

    Optional<AgentAccount> findByAgentNo(String agentNo);
}
