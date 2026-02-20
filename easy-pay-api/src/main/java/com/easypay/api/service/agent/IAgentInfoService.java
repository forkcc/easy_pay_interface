package com.easypay.api.service.agent;

import com.easypay.api.dto.agent.AgentInfoDTO;
import com.easypay.api.result.PageResult;
import java.util.List;

public interface IAgentInfoService {

    AgentInfoDTO create(AgentInfoDTO dto);

    AgentInfoDTO getByAgentNo(String agentNo);

    boolean update(AgentInfoDTO dto);

    boolean updateState(String agentNo, Byte state);

    PageResult<AgentInfoDTO> page(AgentInfoDTO query, int pageNum, int pageSize);

    List<AgentInfoDTO> listByParentAgentNo(String parentAgentNo);
}
