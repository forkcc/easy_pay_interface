package com.easypay.provider.repository.sys;

import com.easypay.provider.entity.sys.SysLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysLogRepository extends JpaRepository<SysLog, Long> {

    Page<SysLog> findBySysType(String sysType, Pageable pageable);

    Page<SysLog> findBySysTypeAndUserId(String sysType, String userId, Pageable pageable);
}
