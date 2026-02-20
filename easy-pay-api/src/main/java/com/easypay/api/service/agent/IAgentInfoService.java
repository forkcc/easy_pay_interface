package com.easypay.api.service.agent;

import com.easypay.api.dto.agent.AgentInfoDTO;
import com.easypay.api.result.PageResult;
import java.util.List;

/**
 * 代理商信息服务接口
 */
public interface IAgentInfoService {

    // 创建代理商
    AgentInfoDTO create(AgentInfoDTO dto);

    // 根据代理商编号查询代理商信息
    AgentInfoDTO getByAgentNo(String agentNo);

    // 更新代理商信息
    boolean update(AgentInfoDTO dto);

    // 更新代理商状态
    boolean updateState(String agentNo, Byte state);

    // 分页查询代理商列表
    PageResult<AgentInfoDTO> page(AgentInfoDTO query, int pageNum, int pageSize);

    // 查询指定上级代理商下的子代理商列表
    List<AgentInfoDTO> listByParentAgentNo(String parentAgentNo);
}
