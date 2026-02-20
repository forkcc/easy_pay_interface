package com.easypay.provider.repository.sys;

import com.easypay.provider.entity.sys.SysLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 系统操作日志数据访问接口，对应实体 {@link SysLog}。
 */
public interface SysLogRepository extends JpaRepository<SysLog, Long> {

    Page<SysLog> findBySysType(String sysType, Pageable pageable);

    Page<SysLog> findBySysTypeAndUserId(String sysType, String userId, Pageable pageable);
}
