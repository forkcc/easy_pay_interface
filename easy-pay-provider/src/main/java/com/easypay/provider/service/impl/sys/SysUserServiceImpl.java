package com.easypay.provider.service.impl.sys;

import com.easypay.api.dto.sys.SysUserDTO;
import com.easypay.api.result.PageResult;
import com.easypay.api.service.sys.ISysUserService;
import com.easypay.provider.converter.SysConverter;
import com.easypay.provider.entity.sys.SysUser;
import com.easypay.provider.repository.sys.SysUserRepository;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统用户服务实现，实现 {@link ISysUserService} 接口，提供系统用户的创建、更新、删除、密码修改及分页查询功能
 */
@DubboService
public class SysUserServiceImpl implements ISysUserService {

    @Resource
    private SysUserRepository sysUserRepository;

    @Override
    @Transactional
    public SysUserDTO create(SysUserDTO dto, String loginPassword) {
        SysUser entity = SysConverter.toEntity(dto);
        entity.setLoginPassword(loginPassword);
        entity = sysUserRepository.save(entity);
        return SysConverter.toDTO(entity);
    }

    @Override
    public SysUserDTO getById(Long sysUserId) {
        return sysUserRepository.findById(sysUserId).map(SysConverter::toDTO).orElse(null);
    }

    @Override
    public SysUserDTO getByUsername(String loginUsername, Byte userType) {
        return sysUserRepository.findByLoginUsernameAndUserType(loginUsername, userType).map(SysConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean update(SysUserDTO dto) {
        return sysUserRepository.findById(dto.getSysUserId()).map(entity -> {
            entity.setRealname(dto.getRealname());
            entity.setTelphone(dto.getTelphone());
            entity.setSex(dto.getSex());
            entity.setAvatarUrl(dto.getAvatarUrl());
            entity.setState(dto.getState());
            sysUserRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    @Transactional
    public boolean delete(Long sysUserId) {
        sysUserRepository.deleteById(sysUserId);
        return true;
    }

    @Override
    @Transactional
    public boolean updatePassword(Long sysUserId, String newPassword) {
        return sysUserRepository.findById(sysUserId).map(entity -> {
            entity.setLoginPassword(newPassword);
            sysUserRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    public PageResult<SysUserDTO> page(SysUserDTO query, int pageNum, int pageSize) {
        SysUser probe = new SysUser();
        probe.setUserType(query.getUserType());
        probe.setBelongId(query.getBelongId());
        probe.setState(query.getState());
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<SysUser> example = Example.of(probe, matcher);
        Page<SysUser> page = sysUserRepository.findAll(example, PageRequest.of(pageNum - 1, pageSize));
        List<SysUserDTO> dtoList = SysConverter.toSysUserDTOList(page.getContent());
        return new PageResult<>(dtoList, page.getTotalElements(), pageNum, pageSize);
    }
}
