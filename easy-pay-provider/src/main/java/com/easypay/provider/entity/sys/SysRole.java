package com.easypay.provider.entity.sys;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 系统角色实体，对应表 t_sys_role。
 */
@Entity
@Table(name = "t_sys_role")
public class SysRole extends BaseEntity {

    @Id
    @Column(name = "role_id", length = 30)
    private String roleId;

    @Column(name = "role_name", length = 30, nullable = false)
    private String roleName;

    @Column(name = "belong_type", length = 20)
    private String belongType;

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
}
