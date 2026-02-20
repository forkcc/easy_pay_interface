package com.easypay.provider.repository.sys;

import com.easypay.provider.entity.sys.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    Optional<SysUser> findByLoginUsernameAndUserType(String loginUsername, Byte userType);
}
