package com.easypay.api.service.sys;

import com.easypay.api.dto.sys.SysUserDTO;
import com.easypay.api.result.PageResult;

public interface ISysUserService {

    SysUserDTO create(SysUserDTO dto, String loginPassword);

    SysUserDTO getById(Long sysUserId);

    SysUserDTO getByUsername(String loginUsername, Byte userType);

    boolean update(SysUserDTO dto);

    boolean delete(Long sysUserId);

    boolean updatePassword(Long sysUserId, String newPassword);

    PageResult<SysUserDTO> page(SysUserDTO query, int pageNum, int pageSize);
}
