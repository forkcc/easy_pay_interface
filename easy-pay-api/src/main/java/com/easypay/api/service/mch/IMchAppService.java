package com.easypay.api.service.mch;

import com.easypay.api.dto.mch.MchAppDTO;
import com.easypay.api.result.PageResult;

import java.util.List;

/**
 * 商户应用服务接口
 */
public interface IMchAppService {

    // 创建商户应用
    MchAppDTO create(MchAppDTO dto);

    // 根据应用ID查询应用信息
    MchAppDTO getByAppId(String appId);

    // 更新商户应用信息
    boolean update(MchAppDTO dto);

    // 删除商户应用
    boolean delete(String appId);

    // 重置应用密钥
    String resetAppSecret(String appId);

    // 查询指定商户下的所有应用
    List<MchAppDTO> listByMchNo(String mchNo);

    // 分页查询商户应用列表
    PageResult<MchAppDTO> page(String mchNo, int pageNum, int pageSize);
}
