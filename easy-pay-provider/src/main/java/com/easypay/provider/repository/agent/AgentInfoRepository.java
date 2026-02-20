package com.easypay.provider.repository.agent;

import com.easypay.provider.entity.agent.AgentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentInfoRepository extends JpaRepository<AgentInfo, String> {

    List<AgentInfo> findByParentAgentNo(String parentAgentNo);
}
