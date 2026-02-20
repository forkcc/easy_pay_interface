package com.easypay.api.service.sys;

import com.easypay.api.dto.sys.SysEntitlementDTO;
import com.easypay.api.dto.sys.SysRoleDTO;
import java.util.List;

/**
 * 系统权限认证服务接口
 */
public interface ISysAuthService {

    // 创建角色
    SysRoleDTO createRole(SysRoleDTO dto);

    // 根据角色ID查询角色
    SysRoleDTO getRoleById(String roleId);

    // 更新角色
    boolean updateRole(SysRoleDTO dto);

    // 删除角色
    boolean deleteRole(String roleId);

    // 按所属类型查询角色列表
    List<SysRoleDTO> listRole(String belongType);

    // 创建权限资源
    SysEntitlementDTO createEntitlement(SysEntitlementDTO dto);

    // 根据权限ID查询权限资源
    SysEntitlementDTO getEntitlementById(String entId);

    // 更新权限资源
    boolean updateEntitlement(SysEntitlementDTO dto);

    // 删除权限资源
    boolean deleteEntitlement(String entId);

    // 查询权限资源列表
    List<SysEntitlementDTO> listEntitlement(String sysType, Byte entType);

    // 为角色分配权限
    boolean assignRoleEntitlements(String roleId, List<String> entIds);

    // 查询角色拥有的权限列表
    List<SysEntitlementDTO> listEntitlementsByRoleId(String roleId);

    // 为用户分配角色
    boolean assignUserRoles(Long sysUserId, List<String> roleIds);

    // 查询用户拥有的角色列表
    List<SysRoleDTO> listRolesByUserId(Long sysUserId);
}
