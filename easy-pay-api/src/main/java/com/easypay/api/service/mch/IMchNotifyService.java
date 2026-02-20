package com.easypay.api.service.mch;

import com.easypay.api.dto.mch.MchNotifyDTO;
import com.easypay.api.result.PageResult;

import java.util.List;

public interface IMchNotifyService {

    MchNotifyDTO create(MchNotifyDTO dto);

    MchNotifyDTO getById(Long notifyId);

    boolean updateState(Long notifyId, Byte state, Integer notifyCount);

    List<MchNotifyDTO> listPendingNotify(int limit);

    PageResult<MchNotifyDTO> page(String mchNo, String orderId, int pageNum, int pageSize);
}
