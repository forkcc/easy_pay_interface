package com.easypay.provider.repository.sys;

import com.easypay.provider.entity.sys.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 系统用户角色关联数据访问接口，对应实体 {@link SysUserRole}。
 */
public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {

    List<SysUserRole> findBySysUserId(Long sysUserId);

    void deleteBySysUserId(Long sysUserId);
}
