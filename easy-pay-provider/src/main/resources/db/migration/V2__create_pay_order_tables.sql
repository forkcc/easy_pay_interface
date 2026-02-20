-- =============================================
-- 支付订单域 (Payment Order Domain)
-- Tables: t_pay_order (按月分区), t_refund_order, t_transfer_order
-- t_pay_order 支撑 2000 万级订单量，按 created_at 月份 RANGE 分区
-- =============================================

CREATE TABLE t_pay_order (
    pay_order_id    VARCHAR(30)  NOT NULL COMMENT '支付订单号',
    mch_no          VARCHAR(30)  NOT NULL COMMENT '商户号',
    app_id          VARCHAR(30)  NOT NULL COMMENT '应用ID',
    mch_order_no    VARCHAR(30)  NOT NULL COMMENT '商户订单号',
    way_code        VARCHAR(20)           COMMENT '支付方式代码',
    amount          BIGINT       NOT NULL COMMENT '支付金额(分)',
    currency        VARCHAR(3)   NOT NULL DEFAULT 'CNY' COMMENT '币种',
    state           TINYINT      NOT NULL DEFAULT 0 COMMENT '状态: 0-初始 1-支付中 2-成功 3-失败 4-撤销 5-退款 6-关闭 7-过期',
    client_ip       VARCHAR(45)           COMMENT '客户端IP',
    subject         VARCHAR(128)          COMMENT '商品标题',
    body            VARCHAR(256)          COMMENT '商品描述',
    fee_rate        BIGINT                COMMENT '费率(万分之)',
    fee_amount      BIGINT                COMMENT '手续费(分)',
    channel_order_no VARCHAR(64)          COMMENT '渠道订单号',
    channel_extra   TEXT                  COMMENT '渠道扩展参数(JSON)',
    notify_url      VARCHAR(256)          COMMENT '异步通知地址',
    return_url      VARCHAR(256)          COMMENT '同步跳转地址',
    err_code        VARCHAR(64)           COMMENT '错误码',
    err_msg         VARCHAR(256)          COMMENT '错误描述',
    ext_param       TEXT                  COMMENT '扩展参数(JSON)',
    expired_time    DATETIME              COMMENT '过期时间',
    success_time    DATETIME              COMMENT '成功时间',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (pay_order_id, created_at),
    INDEX idx_pay_order_mch_state (mch_no, state, created_at),
    INDEX idx_pay_order_mch_order (mch_no, mch_order_no),
    INDEX idx_pay_order_created (created_at, state),
    INDEX idx_pay_order_app (app_id, state)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付订单表(按月分区)'
PARTITION BY RANGE (TO_DAYS(created_at)) (
    PARTITION p202601 VALUES LESS THAN (TO_DAYS('2026-02-01')),
    PARTITION p202602 VALUES LESS THAN (TO_DAYS('2026-03-01')),
    PARTITION p202603 VALUES LESS THAN (TO_DAYS('2026-04-01')),
    PARTITION p202604 VALUES LESS THAN (TO_DAYS('2026-05-01')),
    PARTITION p202605 VALUES LESS THAN (TO_DAYS('2026-06-01')),
    PARTITION p202606 VALUES LESS THAN (TO_DAYS('2026-07-01')),
    PARTITION p202607 VALUES LESS THAN (TO_DAYS('2026-08-01')),
    PARTITION p202608 VALUES LESS THAN (TO_DAYS('2026-09-01')),
    PARTITION p202609 VALUES LESS THAN (TO_DAYS('2026-10-01')),
    PARTITION p202610 VALUES LESS THAN (TO_DAYS('2026-11-01')),
    PARTITION p202611 VALUES LESS THAN (TO_DAYS('2026-12-01')),
    PARTITION p202612 VALUES LESS THAN (TO_DAYS('2027-01-01')),
    PARTITION p_future VALUES LESS THAN MAXVALUE
);

CREATE TABLE t_refund_order (
    refund_order_id  VARCHAR(30)  NOT NULL COMMENT '退款订单号',
    pay_order_id     VARCHAR(30)  NOT NULL COMMENT '原支付订单号',
    mch_no           VARCHAR(30)  NOT NULL COMMENT '商户号',
    app_id           VARCHAR(30)           COMMENT '应用ID',
    mch_refund_no    VARCHAR(30)           COMMENT '商户退款单号',
    pay_amount       BIGINT                COMMENT '原支付金额(分)',
    refund_amount    BIGINT       NOT NULL COMMENT '退款金额(分)',
    currency         VARCHAR(3)   NOT NULL DEFAULT 'CNY' COMMENT '币种',
    state            TINYINT      NOT NULL DEFAULT 0 COMMENT '状态: 0-初始 1-退款中 2-成功 3-失败 4-关闭',
    channel_order_no VARCHAR(64)           COMMENT '渠道支付订单号',
    channel_refund_no VARCHAR(64)          COMMENT '渠道退款单号',
    reason           VARCHAR(256)          COMMENT '退款原因',
    err_code         VARCHAR(64)           COMMENT '错误码',
    err_msg          VARCHAR(256)          COMMENT '错误描述',
    notify_url       VARCHAR(256)          COMMENT '异步通知地址',
    ext_param        TEXT                  COMMENT '扩展参数(JSON)',
    success_time     DATETIME              COMMENT '退款成功时间',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (refund_order_id),
    INDEX idx_refund_pay_order (pay_order_id),
    INDEX idx_refund_mch (mch_no, state)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款订单表';

CREATE TABLE t_transfer_order (
    transfer_id      VARCHAR(30)  NOT NULL COMMENT '转账单号',
    mch_no           VARCHAR(30)  NOT NULL COMMENT '商户号',
    app_id           VARCHAR(30)           COMMENT '应用ID',
    mch_transfer_no  VARCHAR(30)           COMMENT '商户转账单号',
    way_code         VARCHAR(20)           COMMENT '支付方式代码',
    amount           BIGINT       NOT NULL COMMENT '转账金额(分)',
    currency         VARCHAR(3)   NOT NULL DEFAULT 'CNY' COMMENT '币种',
    state            TINYINT      NOT NULL DEFAULT 0 COMMENT '状态: 0-初始 1-转账中 2-成功 3-失败 4-关闭',
    account_no       VARCHAR(64)           COMMENT '收款账号',
    account_name     VARCHAR(64)           COMMENT '收款人姓名',
    bank_name        VARCHAR(64)           COMMENT '收款银行',
    channel_order_no VARCHAR(64)           COMMENT '渠道订单号',
    err_code         VARCHAR(64)           COMMENT '错误码',
    err_msg          VARCHAR(256)          COMMENT '错误描述',
    ext_param        TEXT                  COMMENT '扩展参数(JSON)',
    success_time     DATETIME              COMMENT '成功时间',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (transfer_id),
    INDEX idx_transfer_mch (mch_no, state)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='转账订单表';
