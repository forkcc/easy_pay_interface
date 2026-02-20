package com.easypay.api.service.mch;

import com.easypay.api.dto.mch.MchAppDTO;
import com.easypay.api.result.PageResult;

import java.util.List;

public interface IMchAppService {

    MchAppDTO create(MchAppDTO dto);

    MchAppDTO getByAppId(String appId);

    boolean update(MchAppDTO dto);

    boolean delete(String appId);

    String resetAppSecret(String appId);

    List<MchAppDTO> listByMchNo(String mchNo);

    PageResult<MchAppDTO> page(String mchNo, int pageNum, int pageSize);
}
