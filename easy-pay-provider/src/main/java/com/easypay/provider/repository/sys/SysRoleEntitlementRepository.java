package com.easypay.provider.repository.sys;

import com.easypay.provider.entity.sys.SysRoleEntitlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysRoleEntitlementRepository extends JpaRepository<SysRoleEntitlement, Long> {

    List<SysRoleEntitlement> findByRoleId(String roleId);

    void deleteByRoleId(String roleId);
}
