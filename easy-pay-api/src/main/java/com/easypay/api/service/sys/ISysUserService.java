package com.easypay.api.service.sys;

import com.easypay.api.dto.sys.SysUserDTO;
import com.easypay.api.result.PageResult;

/**
 * 系统用户服务接口
 */
public interface ISysUserService {

    // 创建系统用户
    SysUserDTO create(SysUserDTO dto, String loginPassword);

    // 根据用户ID查询用户
    SysUserDTO getById(Long sysUserId);

    // 根据用户名和用户类型查询用户
    SysUserDTO getByUsername(String loginUsername, Byte userType);

    // 更新用户信息
    boolean update(SysUserDTO dto);

    // 删除用户
    boolean delete(Long sysUserId);

    // 修改密码
    boolean updatePassword(Long sysUserId, String newPassword);

    // 分页查询用户列表
    PageResult<SysUserDTO> page(SysUserDTO query, int pageNum, int pageSize);
}
