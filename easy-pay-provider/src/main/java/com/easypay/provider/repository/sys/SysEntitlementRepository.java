package com.easypay.provider.repository.sys;

import com.easypay.provider.entity.sys.SysEntitlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 系统权限数据访问接口，对应实体 {@link SysEntitlement}。
 */
public interface SysEntitlementRepository extends JpaRepository<SysEntitlement, String> {

    List<SysEntitlement> findBySysType(String sysType);

    List<SysEntitlement> findBySysTypeAndEntType(String sysType, Byte entType);
}
