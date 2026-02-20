-- =============================================
-- 商户域 (Merchant Domain)
-- Tables: t_mch_info, t_mch_app, t_mch_notify
-- =============================================

CREATE TABLE t_mch_info (
    mch_no          VARCHAR(30)  NOT NULL COMMENT '商户号',
    mch_name        VARCHAR(60)  NOT NULL COMMENT '商户名称',
    mch_short_name  VARCHAR(30)           COMMENT '商户简称',
    type            TINYINT               COMMENT '商户类型: 1-普通 2-个体 3-企业',
    state           TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-停用 1-正常',
    agent_no        VARCHAR(30)           COMMENT '所属代理商号',
    contact_name    VARCHAR(30)           COMMENT '联系人姓名',
    contact_tel     VARCHAR(20)           COMMENT '联系人电话',
    contact_email   VARCHAR(50)           COMMENT '联系人邮箱',
    api_key         TEXT                  COMMENT 'API密钥',
    remark          VARCHAR(256)          COMMENT '备注',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (mch_no),
    INDEX idx_mch_info_agent (agent_no),
    INDEX idx_mch_info_state (state)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户信息表';

CREATE TABLE t_mch_app (
    app_id          VARCHAR(30)  NOT NULL COMMENT '应用ID',
    mch_no          VARCHAR(30)  NOT NULL COMMENT '商户号',
    app_name        VARCHAR(60)           COMMENT '应用名称',
    state           TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-停用 1-正常',
    app_secret      TEXT                  COMMENT '应用私钥',
    notify_url      VARCHAR(256)          COMMENT '异步通知地址',
    return_url      VARCHAR(256)          COMMENT '同步跳转地址',
    remark          VARCHAR(256)          COMMENT '备注',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (app_id),
    INDEX idx_mch_app_mch (mch_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户应用表';

CREATE TABLE t_mch_notify (
    notify_id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '通知ID',
    order_id           VARCHAR(30)           COMMENT '订单号',
    order_type         VARCHAR(20)           COMMENT '订单类型: pay-支付 refund-退款',
    mch_no             VARCHAR(30)           COMMENT '商户号',
    mch_order_no       VARCHAR(30)           COMMENT '商户订单号',
    notify_url         VARCHAR(256)          COMMENT '通知地址',
    state              TINYINT      NOT NULL DEFAULT 0 COMMENT '状态: 0-初始 1-通知中 2-成功 3-失败',
    notify_count       INT          NOT NULL DEFAULT 0 COMMENT '已通知次数',
    notify_count_limit BIGINT                DEFAULT 6 COMMENT '最大通知次数',
    last_notify_time   DATETIME              COMMENT '最后通知时间',
    created_at         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (notify_id),
    INDEX idx_mch_notify_order (order_id),
    INDEX idx_mch_notify_mch (mch_no),
    INDEX idx_mch_notify_state (state, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户通知记录表';
