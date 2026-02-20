package com.easypay.provider.repository.sys;

import com.easypay.provider.entity.sys.SysEntitlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysEntitlementRepository extends JpaRepository<SysEntitlement, String> {

    List<SysEntitlement> findBySysType(String sysType);

    List<SysEntitlement> findBySysTypeAndEntType(String sysType, Byte entType);
}
