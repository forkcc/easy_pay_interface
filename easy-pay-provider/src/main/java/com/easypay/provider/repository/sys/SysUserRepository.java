package com.easypay.provider.repository.sys;

import com.easypay.provider.entity.sys.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 系统用户数据访问接口，对应实体 {@link SysUser}。
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    Optional<SysUser> findByLoginUsernameAndUserType(String loginUsername, Byte userType);
}
