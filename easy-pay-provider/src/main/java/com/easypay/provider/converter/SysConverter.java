package com.easypay.provider.converter;

import com.easypay.api.dto.sys.SysConfigDTO;
import com.easypay.api.dto.sys.SysEntitlementDTO;
import com.easypay.api.dto.sys.SysLogDTO;
import com.easypay.api.dto.sys.SysRoleDTO;
import com.easypay.api.dto.sys.SysUserDTO;
import com.easypay.provider.entity.sys.SysConfig;
import com.easypay.provider.entity.sys.SysEntitlement;
import com.easypay.provider.entity.sys.SysLog;
import com.easypay.provider.entity.sys.SysRole;
import com.easypay.provider.entity.sys.SysUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SysConverter {

    public static SysUserDTO toDTO(SysUser entity) {
        if (entity == null) {
            return null;
        }
        SysUserDTO dto = new SysUserDTO();
        dto.setSysUserId(entity.getSysUserId());
        dto.setLoginUsername(entity.getLoginUsername());
        dto.setRealname(entity.getRealname());
        dto.setTelphone(entity.getTelphone());
        dto.setSex(entity.getSex());
        dto.setAvatarUrl(entity.getAvatarUrl());
        dto.setUserType(entity.getUserType());
        dto.setBelongId(entity.getBelongId());
        dto.setState(entity.getState());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static SysUser toEntity(SysUserDTO dto) {
        if (dto == null) {
            return null;
        }
        SysUser entity = new SysUser();
        entity.setSysUserId(dto.getSysUserId());
        entity.setLoginUsername(dto.getLoginUsername());
        entity.setRealname(dto.getRealname());
        entity.setTelphone(dto.getTelphone());
        entity.setSex(dto.getSex());
        entity.setAvatarUrl(dto.getAvatarUrl());
        entity.setUserType(dto.getUserType());
        entity.setBelongId(dto.getBelongId());
        entity.setState(dto.getState());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<SysUserDTO> toSysUserDTOList(List<SysUser> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysUserDTO> result = new ArrayList<>(entities.size());
        for (SysUser entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static SysRoleDTO toDTO(SysRole entity) {
        if (entity == null) {
            return null;
        }
        SysRoleDTO dto = new SysRoleDTO();
        dto.setRoleId(entity.getRoleId());
        dto.setRoleName(entity.getRoleName());
        dto.setBelongType(entity.getBelongType());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static SysRole toEntity(SysRoleDTO dto) {
        if (dto == null) {
            return null;
        }
        SysRole entity = new SysRole();
        entity.setRoleId(dto.getRoleId());
        entity.setRoleName(dto.getRoleName());
        entity.setBelongType(dto.getBelongType());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<SysRoleDTO> toSysRoleDTOList(List<SysRole> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysRoleDTO> result = new ArrayList<>(entities.size());
        for (SysRole entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static SysEntitlementDTO toDTO(SysEntitlement entity) {
        if (entity == null) {
            return null;
        }
        SysEntitlementDTO dto = new SysEntitlementDTO();
        dto.setEntId(entity.getEntId());
        dto.setEntName(entity.getEntName());
        dto.setEntType(entity.getEntType());
        dto.setMenuIcon(entity.getMenuIcon());
        dto.setMenuUri(entity.getMenuUri());
        dto.setParentId(entity.getParentId());
        dto.setEntSort(entity.getEntSort());
        dto.setState(entity.getState());
        dto.setSysType(entity.getSysType());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static SysEntitlement toEntity(SysEntitlementDTO dto) {
        if (dto == null) {
            return null;
        }
        SysEntitlement entity = new SysEntitlement();
        entity.setEntId(dto.getEntId());
        entity.setEntName(dto.getEntName());
        entity.setEntType(dto.getEntType());
        entity.setMenuIcon(dto.getMenuIcon());
        entity.setMenuUri(dto.getMenuUri());
        entity.setParentId(dto.getParentId());
        entity.setEntSort(dto.getEntSort());
        entity.setState(dto.getState());
        entity.setSysType(dto.getSysType());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<SysEntitlementDTO> toSysEntitlementDTOList(List<SysEntitlement> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysEntitlementDTO> result = new ArrayList<>(entities.size());
        for (SysEntitlement entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static SysConfigDTO toDTO(SysConfig entity) {
        if (entity == null) {
            return null;
        }
        SysConfigDTO dto = new SysConfigDTO();
        dto.setConfigKey(entity.getConfigKey());
        dto.setConfigName(entity.getConfigName());
        dto.setConfigValue(entity.getConfigValue());
        dto.setConfigGroup(entity.getConfigGroup());
        dto.setRemark(entity.getRemark());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static SysConfig toEntity(SysConfigDTO dto) {
        if (dto == null) {
            return null;
        }
        SysConfig entity = new SysConfig();
        entity.setConfigKey(dto.getConfigKey());
        entity.setConfigName(dto.getConfigName());
        entity.setConfigValue(dto.getConfigValue());
        entity.setConfigGroup(dto.getConfigGroup());
        entity.setRemark(dto.getRemark());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<SysConfigDTO> toSysConfigDTOList(List<SysConfig> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysConfigDTO> result = new ArrayList<>(entities.size());
        for (SysConfig entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static SysLogDTO toDTO(SysLog entity) {
        if (entity == null) {
            return null;
        }
        SysLogDTO dto = new SysLogDTO();
        dto.setId(entity.getId());
        dto.setUserType(entity.getUserType());
        dto.setUserId(entity.getUserId());
        dto.setUserName(entity.getUserName());
        dto.setSysType(entity.getSysType());
        dto.setMethodName(entity.getMethodName());
        dto.setMethodRemark(entity.getMethodRemark());
        dto.setReqUrl(entity.getReqUrl());
        dto.setOptReqParam(entity.getOptReqParam());
        dto.setOptResInfo(entity.getOptResInfo());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static SysLog toEntity(SysLogDTO dto) {
        if (dto == null) {
            return null;
        }
        SysLog entity = new SysLog();
        entity.setId(dto.getId());
        entity.setUserType(dto.getUserType());
        entity.setUserId(dto.getUserId());
        entity.setUserName(dto.getUserName());
        entity.setSysType(dto.getSysType());
        entity.setMethodName(dto.getMethodName());
        entity.setMethodRemark(dto.getMethodRemark());
        entity.setReqUrl(dto.getReqUrl());
        entity.setOptReqParam(dto.getOptReqParam());
        entity.setOptResInfo(dto.getOptResInfo());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }

    public static List<SysLogDTO> toSysLogDTOList(List<SysLog> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysLogDTO> result = new ArrayList<>(entities.size());
        for (SysLog entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }
}
