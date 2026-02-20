package com.easypay.api.service.mch;

import com.easypay.api.dto.mch.MchNotifyDTO;
import com.easypay.api.result.PageResult;

import java.util.List;

/**
 * 商户通知服务接口
 */
public interface IMchNotifyService {

    // 创建通知记录
    MchNotifyDTO create(MchNotifyDTO dto);

    // 根据通知ID查询通知记录
    MchNotifyDTO getById(Long notifyId);

    // 更新通知状态及通知次数
    boolean updateState(Long notifyId, Byte state, Integer notifyCount);

    // 查询待通知列表
    List<MchNotifyDTO> listPendingNotify(int limit);

    // 分页查询通知记录
    PageResult<MchNotifyDTO> page(String mchNo, String orderId, int pageNum, int pageSize);
}
