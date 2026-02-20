package com.easypay.provider.repository.sys;

import com.easypay.provider.entity.sys.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 系统角色数据访问接口，对应实体 {@link SysRole}。
 */
public interface SysRoleRepository extends JpaRepository<SysRole, String> {

    List<SysRole> findByBelongType(String belongType);
}
