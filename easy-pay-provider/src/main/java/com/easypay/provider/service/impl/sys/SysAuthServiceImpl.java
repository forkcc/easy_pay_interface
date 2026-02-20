package com.easypay.provider.service.impl.sys;

import com.easypay.api.dto.sys.SysEntitlementDTO;
import com.easypay.api.dto.sys.SysRoleDTO;
import com.easypay.api.service.sys.ISysAuthService;
import com.easypay.provider.converter.SysConverter;
import com.easypay.provider.entity.sys.SysEntitlement;
import com.easypay.provider.entity.sys.SysRole;
import com.easypay.provider.entity.sys.SysRoleEntitlement;
import com.easypay.provider.entity.sys.SysUserRole;
import com.easypay.provider.repository.sys.SysEntitlementRepository;
import com.easypay.provider.repository.sys.SysRoleEntitlementRepository;
import com.easypay.provider.repository.sys.SysRoleRepository;
import com.easypay.provider.repository.sys.SysUserRoleRepository;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统权限服务实现，实现 {@link ISysAuthService} 接口，提供角色、权限资源的管理以及用户角色和角色权限的分配功能
 */
@DubboService
public class SysAuthServiceImpl implements ISysAuthService {

    @Resource
    private SysRoleRepository sysRoleRepository;

    @Resource
    private SysEntitlementRepository sysEntitlementRepository;

    @Resource
    private SysRoleEntitlementRepository sysRoleEntitlementRepository;

    @Resource
    private SysUserRoleRepository sysUserRoleRepository;

    @Override
    @Transactional
    public SysRoleDTO createRole(SysRoleDTO dto) {
        SysRole entity = SysConverter.toEntity(dto);
        entity = sysRoleRepository.save(entity);
        return SysConverter.toDTO(entity);
    }

    @Override
    public SysRoleDTO getRoleById(String roleId) {
        return sysRoleRepository.findById(roleId).map(SysConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean updateRole(SysRoleDTO dto) {
        return sysRoleRepository.findById(dto.getRoleId()).map(entity -> {
            entity.setRoleName(dto.getRoleName());
            entity.setBelongType(dto.getBelongType());
            sysRoleRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    @Transactional
    public boolean deleteRole(String roleId) {
        sysRoleEntitlementRepository.deleteByRoleId(roleId);
        sysRoleRepository.deleteById(roleId);
        return true;
    }

    @Override
    public List<SysRoleDTO> listRole(String belongType) {
        return SysConverter.toSysRoleDTOList(sysRoleRepository.findByBelongType(belongType));
    }

    @Override
    @Transactional
    public SysEntitlementDTO createEntitlement(SysEntitlementDTO dto) {
        SysEntitlement entity = SysConverter.toEntity(dto);
        entity = sysEntitlementRepository.save(entity);
        return SysConverter.toDTO(entity);
    }

    @Override
    public SysEntitlementDTO getEntitlementById(String entId) {
        return sysEntitlementRepository.findById(entId).map(SysConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean updateEntitlement(SysEntitlementDTO dto) {
        return sysEntitlementRepository.findById(dto.getEntId()).map(entity -> {
            entity.setEntName(dto.getEntName());
            entity.setEntType(dto.getEntType());
            entity.setMenuIcon(dto.getMenuIcon());
            entity.setMenuUri(dto.getMenuUri());
            entity.setParentId(dto.getParentId());
            entity.setEntSort(dto.getEntSort());
            entity.setState(dto.getState());
            entity.setSysType(dto.getSysType());
            sysEntitlementRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    @Transactional
    public boolean deleteEntitlement(String entId) {
        sysEntitlementRepository.deleteById(entId);
        return true;
    }

    @Override
    public List<SysEntitlementDTO> listEntitlement(String sysType, Byte entType) {
        List<SysEntitlement> list;
        if (entType != null) {
            list = sysEntitlementRepository.findBySysTypeAndEntType(sysType, entType);
        } else {
            list = sysEntitlementRepository.findBySysType(sysType);
        }
        return SysConverter.toSysEntitlementDTOList(list);
    }

    @Override
    @Transactional
    public boolean assignRoleEntitlements(String roleId, List<String> entIds) {
        sysRoleEntitlementRepository.deleteByRoleId(roleId);
        if (entIds != null && !entIds.isEmpty()) {
            List<SysRoleEntitlement> entities = new ArrayList<>();
            for (String entId : entIds) {
                SysRoleEntitlement re = new SysRoleEntitlement();
                re.setRoleId(roleId);
                re.setEntId(entId);
                entities.add(re);
            }
            sysRoleEntitlementRepository.saveAll(entities);
        }
        return true;
    }

    @Override
    public List<SysEntitlementDTO> listEntitlementsByRoleId(String roleId) {
        List<SysRoleEntitlement> relations = sysRoleEntitlementRepository.findByRoleId(roleId);
        List<String> entIds = relations.stream().map(SysRoleEntitlement::getEntId).collect(Collectors.toList());
        if (entIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<SysEntitlement> entitlements = sysEntitlementRepository.findAllById(entIds);
        return SysConverter.toSysEntitlementDTOList(entitlements);
    }

    @Override
    @Transactional
    public boolean assignUserRoles(Long sysUserId, List<String> roleIds) {
        sysUserRoleRepository.deleteBySysUserId(sysUserId);
        if (roleIds != null && !roleIds.isEmpty()) {
            List<SysUserRole> entities = new ArrayList<>();
            for (String roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setSysUserId(sysUserId);
                ur.setRoleId(roleId);
                entities.add(ur);
            }
            sysUserRoleRepository.saveAll(entities);
        }
        return true;
    }

    @Override
    public List<SysRoleDTO> listRolesByUserId(Long sysUserId) {
        List<SysUserRole> relations = sysUserRoleRepository.findBySysUserId(sysUserId);
        List<String> roleIds = relations.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        if (roleIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<SysRole> roles = sysRoleRepository.findAllById(roleIds);
        return SysConverter.toSysRoleDTOList(roles);
    }
}
