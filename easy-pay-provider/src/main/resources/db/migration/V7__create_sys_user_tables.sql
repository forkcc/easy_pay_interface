-- =============================================
-- 系统管理域 - 用户与权限 (System - User & RBAC)
-- Tables: t_sys_user, t_sys_role, t_sys_entitlement,
--         t_sys_user_role, t_sys_role_entitlement
-- =============================================

CREATE TABLE t_sys_user (
    sys_user_id      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    login_username   VARCHAR(30)  NOT NULL COMMENT '登录用户名',
    login_password   VARCHAR(128) NOT NULL COMMENT '登录密码(BCrypt)',
    realname         VARCHAR(30)           COMMENT '真实姓名',
    telphone         VARCHAR(20)           COMMENT '手机号',
    sex              TINYINT               COMMENT '性别: 0-未知 1-男 2-女',
    avatar_url       VARCHAR(256)          COMMENT '头像地址',
    user_type        TINYINT      NOT NULL COMMENT '用户类型: 1-运营 2-商户 3-代理商',
    belong_id        VARCHAR(30)           COMMENT '所属ID(运营为0, 商户为mchNo, 代理商为agentNo)',
    state            TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-停用 1-正常',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (sys_user_id),
    UNIQUE KEY uk_username_type (login_username, user_type),
    INDEX idx_user_belong (user_type, belong_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

CREATE TABLE t_sys_role (
    role_id          VARCHAR(30)  NOT NULL COMMENT '角色ID',
    role_name        VARCHAR(30)  NOT NULL COMMENT '角色名称',
    belong_type      VARCHAR(20)           COMMENT '所属类型: MGR-运营 MCH-商户 AGENT-代理商',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (role_id),
    INDEX idx_role_belong (belong_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

CREATE TABLE t_sys_entitlement (
    ent_id           VARCHAR(30)  NOT NULL COMMENT '权限ID',
    ent_name         VARCHAR(60)  NOT NULL COMMENT '权限名称',
    ent_type         TINYINT      NOT NULL COMMENT '权限类型: 1-菜单 2-按钮',
    menu_icon        VARCHAR(60)           COMMENT '菜单图标',
    menu_uri         VARCHAR(128)          COMMENT '菜单URI',
    parent_id        VARCHAR(30)           COMMENT '父级权限ID',
    ent_sort         INT          NOT NULL DEFAULT 0 COMMENT '排序值',
    state            TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-停用 1-正常',
    sys_type         VARCHAR(20)           COMMENT '系统类型: MGR-运营 MCH-商户 AGENT-代理商',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (ent_id),
    INDEX idx_ent_sys (sys_type, ent_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统权限表';

CREATE TABLE t_sys_user_role (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    sys_user_id      BIGINT       NOT NULL COMMENT '用户ID',
    role_id          VARCHAR(30)  NOT NULL COMMENT '角色ID',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_user_role_user (sys_user_id),
    INDEX idx_user_role_role (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

CREATE TABLE t_sys_role_entitlement (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    role_id          VARCHAR(30)  NOT NULL COMMENT '角色ID',
    ent_id           VARCHAR(30)  NOT NULL COMMENT '权限ID',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_role_ent_role (role_id),
    INDEX idx_role_ent_ent (ent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';
