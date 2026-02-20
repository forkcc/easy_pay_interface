package com.easypay.api.service.mch;

import com.easypay.api.dto.mch.MchInfoDTO;
import com.easypay.api.result.PageResult;

public interface IMchInfoService {

    MchInfoDTO create(MchInfoDTO dto);

    MchInfoDTO getByMchNo(String mchNo);

    boolean update(MchInfoDTO dto);

    boolean updateState(String mchNo, Byte state);

    PageResult<MchInfoDTO> page(MchInfoDTO query, int pageNum, int pageSize);

    PageResult<MchInfoDTO> pageByAgentNo(String agentNo, int pageNum, int pageSize);
}
