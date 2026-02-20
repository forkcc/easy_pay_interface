package com.easypay.provider.repository.sys;

import com.easypay.provider.entity.sys.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysRoleRepository extends JpaRepository<SysRole, String> {

    List<SysRole> findByBelongType(String belongType);
}
