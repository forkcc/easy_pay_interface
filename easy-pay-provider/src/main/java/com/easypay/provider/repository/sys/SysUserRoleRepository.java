package com.easypay.provider.repository.sys;

import com.easypay.provider.entity.sys.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {

    List<SysUserRole> findBySysUserId(Long sysUserId);

    void deleteBySysUserId(Long sysUserId);
}
