package com.easypay.api.service.sys;

import com.easypay.api.dto.sys.SysConfigDTO;
import com.easypay.api.dto.sys.SysLogDTO;
import com.easypay.api.result.PageResult;
import java.util.List;

/**
 * 系统配置与日志服务接口
 */
public interface ISysConfigService {

    // 根据配置键查询配置
    SysConfigDTO getByKey(String configKey);

    // 保存系统配置
    boolean save(SysConfigDTO dto);

    // 根据配置键删除配置
    boolean deleteByKey(String configKey);

    // 按分组查询配置列表
    List<SysConfigDTO> listByGroup(String configGroup);

    // 创建操作日志
    void createLog(SysLogDTO dto);

    // 分页查询操作日志
    PageResult<SysLogDTO> pageLog(String sysType, String userId, int pageNum, int pageSize);
}
