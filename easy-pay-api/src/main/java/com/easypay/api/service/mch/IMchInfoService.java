package com.easypay.api.service.mch;

import com.easypay.api.dto.mch.MchInfoDTO;
import com.easypay.api.result.PageResult;

/**
 * 商户信息服务接口
 */
public interface IMchInfoService {

    // 创建商户
    MchInfoDTO create(MchInfoDTO dto);

    // 根据商户编号查询商户信息
    MchInfoDTO getByMchNo(String mchNo);

    // 更新商户信息
    boolean update(MchInfoDTO dto);

    // 更新商户状态
    boolean updateState(String mchNo, Byte state);

    // 分页查询商户列表
    PageResult<MchInfoDTO> page(MchInfoDTO query, int pageNum, int pageSize);

    // 根据代理商编号分页查询商户列表
    PageResult<MchInfoDTO> pageByAgentNo(String agentNo, int pageNum, int pageSize);
}
