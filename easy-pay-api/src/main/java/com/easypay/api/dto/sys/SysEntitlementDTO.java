package com.easypay.api.dto.sys;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统权限资源数据传输对象
 */
public class SysEntitlementDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 权限ID
    private String entId;
    // 权限名称
    private String entName;
    // 权限类型
    private Byte entType;
    // 菜单图标
    private String menuIcon;
    // 菜单路径
    private String menuUri;
    // 父级权限ID
    private String parentId;
    // 排序值
    private Integer entSort;
    // 状态
    private Byte state;
    // 系统类型
    private String sysType;
    // 创建时间
    private LocalDateTime createdAt;
    // 更新时间
    private LocalDateTime updatedAt;

    public SysEntitlementDTO() {
    }

    public String getEntId() {
        return entId;
    }

    public void setEntId(String entId) {
        this.entId = entId;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public Byte getEntType() {
        return entType;
    }

    public void setEntType(Byte entType) {
        this.entType = entType;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuUri() {
        return menuUri;
    }

    public void setMenuUri(String menuUri) {
        this.menuUri = menuUri;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getEntSort() {
        return entSort;
    }

    public void setEntSort(Integer entSort) {
        this.entSort = entSort;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
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
