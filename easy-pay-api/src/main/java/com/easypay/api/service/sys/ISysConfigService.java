package com.easypay.api.service.sys;

import com.easypay.api.dto.sys.SysConfigDTO;
import com.easypay.api.dto.sys.SysLogDTO;
import com.easypay.api.result.PageResult;
import java.util.List;

public interface ISysConfigService {

    SysConfigDTO getByKey(String configKey);

    boolean save(SysConfigDTO dto);

    boolean deleteByKey(String configKey);

    List<SysConfigDTO> listByGroup(String configGroup);

    void createLog(SysLogDTO dto);

    PageResult<SysLogDTO> pageLog(String sysType, String userId, int pageNum, int pageSize);
}
