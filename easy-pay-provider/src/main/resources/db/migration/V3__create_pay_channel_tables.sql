-- =============================================
-- 支付通道域 (Payment Channel Domain)
-- Tables: t_pay_way, t_pay_passage, t_pay_interface_define,
--         t_pay_interface_config, t_mch_pay_passage
-- =============================================

CREATE TABLE t_pay_way (
    way_code         VARCHAR(20)  NOT NULL COMMENT '支付方式代码',
    way_name         VARCHAR(30)  NOT NULL COMMENT '支付方式名称',
    state            TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-停用 1-正常',
    remark           VARCHAR(128)          COMMENT '备注',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (way_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付方式定义表';

CREATE TABLE t_pay_passage (
    passage_id       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '通道ID',
    passage_name     VARCHAR(60)           COMMENT '通道名称',
    if_code          VARCHAR(20)           COMMENT '接口代码',
    way_code         VARCHAR(20)           COMMENT '支付方式代码',
    fee_rate         BIGINT                COMMENT '费率(万分之)',
    fee_amount       BIGINT                COMMENT '固定手续费(分)',
    state            TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-停用 1-正常',
    remark           VARCHAR(256)          COMMENT '备注',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (passage_id),
    INDEX idx_passage_state (state)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付通道表';

CREATE TABLE t_pay_interface_define (
    if_code          VARCHAR(20)  NOT NULL COMMENT '接口代码',
    if_name          VARCHAR(60)  NOT NULL COMMENT '接口名称',
    if_type          TINYINT               COMMENT '接口类型: 1-官方 2-第三方',
    config_page_type VARCHAR(20)           COMMENT '配置页面类型',
    icon             VARCHAR(128)          COMMENT '图标地址',
    bg_color         VARCHAR(20)           COMMENT '背景色',
    state            TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-停用 1-正常',
    remark           VARCHAR(256)          COMMENT '备注',
    config_info      TEXT                  COMMENT '配置定义(JSON): 支付参数字段定义',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (if_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付接口定义表';

CREATE TABLE t_pay_interface_config (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    info_type        VARCHAR(20)  NOT NULL COMMENT '配置信息类型: ISV-服务商 MCH_APP-商户应用',
    info_id          VARCHAR(64)  NOT NULL COMMENT '信息ID: 对应 ISV 号或 appId',
    if_code          VARCHAR(20)  NOT NULL COMMENT '接口代码',
    if_params        TEXT                  COMMENT '接口参数(JSON): 具体的支付密钥等',
    state            TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-停用 1-正常',
    remark           VARCHAR(256)          COMMENT '备注',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_if_config (info_type, info_id, if_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付接口配置表';

CREATE TABLE t_mch_pay_passage (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    mch_no           VARCHAR(30)  NOT NULL COMMENT '商户号',
    app_id           VARCHAR(30)  NOT NULL COMMENT '应用ID',
    way_code         VARCHAR(20)  NOT NULL COMMENT '支付方式代码',
    passage_id       BIGINT                COMMENT '通道ID',
    fee_rate         BIGINT                COMMENT '商户费率(万分之)',
    fee_amount       BIGINT                COMMENT '固定手续费(分)',
    state            TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-停用 1-正常',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_mch_passage (mch_no, app_id, way_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户支付通道关联表';
