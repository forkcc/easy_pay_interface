-- =============================================
-- 代理商域 (Agent Domain)
-- Tables: t_agent_info, t_agent_account
-- =============================================

CREATE TABLE t_agent_info (
    agent_no         VARCHAR(30)  NOT NULL COMMENT '代理商号',
    agent_name       VARCHAR(60)  NOT NULL COMMENT '代理商名称',
    level            TINYINT               COMMENT '代理等级: 1-一级 2-二级',
    parent_agent_no  VARCHAR(30)           COMMENT '上级代理商号',
    profit_rate      DECIMAL(10,4)         COMMENT '分润比例',
    state            TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-停用 1-正常',
    contact_name     VARCHAR(30)           COMMENT '联系人姓名',
    contact_tel      VARCHAR(20)           COMMENT '联系人电话',
    contact_email    VARCHAR(50)           COMMENT '联系人邮箱',
    remark           VARCHAR(256)          COMMENT '备注',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (agent_no),
    INDEX idx_agent_parent (parent_agent_no),
    INDEX idx_agent_state (state)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代理商信息表';

CREATE TABLE t_agent_account (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    agent_no         VARCHAR(30)  NOT NULL COMMENT '代理商号',
    balance          BIGINT       NOT NULL DEFAULT 0 COMMENT '可用余额(分)',
    frozen_amount    BIGINT       NOT NULL DEFAULT 0 COMMENT '冻结金额(分)',
    settled_amount   BIGINT       NOT NULL DEFAULT 0 COMMENT '已结算金额(分)',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_agent_account (agent_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代理商账户表';
