package com.easypay.provider.entity.sys;

import com.easypay.provider.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 系统权限实体，对应表 t_sys_entitlement。
 */
@Entity
@Table(name = "t_sys_entitlement")
public class SysEntitlement extends BaseEntity {

    @Id
    @Column(name = "ent_id", length = 30)
    private String entId;

    @Column(name = "ent_name", length = 60, nullable = false)
    private String entName;

    @Column(name = "ent_type", nullable = false)
    private Byte entType;

    @Column(name = "menu_icon", length = 60)
    private String menuIcon;

    @Column(name = "menu_uri", length = 128)
    private String menuUri;

    @Column(name = "parent_id", length = 30)
    private String parentId;

    @Column(name = "ent_sort")
    private Integer entSort = 0;

    @Column(name = "state")
    private Byte state;

    @Column(name = "sys_type", length = 20)
    private String sysType;

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
}
