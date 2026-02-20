package com.easypay.provider.repository.agent;

import com.easypay.provider.entity.agent.AgentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 代理商信息数据访问接口，对应实体 {@link AgentInfo}。
 */
public interface AgentInfoRepository extends JpaRepository<AgentInfo, String> {

    List<AgentInfo> findByParentAgentNo(String parentAgentNo);
}
