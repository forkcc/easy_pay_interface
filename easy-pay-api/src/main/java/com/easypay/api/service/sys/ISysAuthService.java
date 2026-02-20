package com.easypay.api.service.sys;

import com.easypay.api.dto.sys.SysEntitlementDTO;
import com.easypay.api.dto.sys.SysRoleDTO;
import java.util.List;

public interface ISysAuthService {

    SysRoleDTO createRole(SysRoleDTO dto);

    SysRoleDTO getRoleById(String roleId);

    boolean updateRole(SysRoleDTO dto);

    boolean deleteRole(String roleId);

    List<SysRoleDTO> listRole(String belongType);

    SysEntitlementDTO createEntitlement(SysEntitlementDTO dto);

    SysEntitlementDTO getEntitlementById(String entId);

    boolean updateEntitlement(SysEntitlementDTO dto);

    boolean deleteEntitlement(String entId);

    List<SysEntitlementDTO> listEntitlement(String sysType, Byte entType);

    boolean assignRoleEntitlements(String roleId, List<String> entIds);

    List<SysEntitlementDTO> listEntitlementsByRoleId(String roleId);

    boolean assignUserRoles(Long sysUserId, List<String> roleIds);

    List<SysRoleDTO> listRolesByUserId(Long sysUserId);
}
