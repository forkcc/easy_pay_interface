package com.easypay.api.dto.sys;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SysRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String roleId;
    private String roleName;
    private String belongType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SysRoleDTO() {
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getBelongType() {
        return belongType;
    }

    public void setBelongType(String belongType) {
        this.belongType = belongType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
