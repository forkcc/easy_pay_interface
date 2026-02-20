-- =============================================
-- 账户与结算域 (Account & Settlement Domain)
-- Tables: t_account_balance, t_account_history,
--         t_sett_record, t_sett_bank_account
-- =============================================

CREATE TABLE t_account_balance (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    account_no       VARCHAR(30)  NOT NULL COMMENT '账户号(商户号/代理商号)',
    account_name     VARCHAR(60)           COMMENT '账户名称',
    account_type     TINYINT               COMMENT '账户类型: 1-商户 2-代理商',
    balance          BIGINT       NOT NULL DEFAULT 0 COMMENT '可用余额(分)',
    frozen_amount    BIGINT       NOT NULL DEFAULT 0 COMMENT '冻结金额(分)',
    unsettled_amount BIGINT       NOT NULL DEFAULT 0 COMMENT '待结算金额(分)',
    settled_amount   BIGINT       NOT NULL DEFAULT 0 COMMENT '已结算金额(分)',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_account_no (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户余额表';

CREATE TABLE t_account_history (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    account_no       VARCHAR(30)  NOT NULL COMMENT '账户号',
    change_type      TINYINT      NOT NULL COMMENT '变动类型: 1-收入 2-支出 3-冻结 4-解冻 5-结算',
    change_amount    BIGINT       NOT NULL COMMENT '变动金额(分)',
    balance_before   BIGINT       NOT NULL COMMENT '变动前余额(分)',
    balance_after    BIGINT       NOT NULL COMMENT '变动后余额(分)',
    biz_order_no     VARCHAR(30)           COMMENT '关联业务订单号',
    biz_type         VARCHAR(20)           COMMENT '业务类型: pay-支付 refund-退款 settle-结算',
    remark           VARCHAR(256)          COMMENT '备注',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_acct_history_time (account_no, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户变动流水表';

CREATE TABLE t_sett_record (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    sett_no          VARCHAR(30)  NOT NULL COMMENT '结算单号',
    account_no       VARCHAR(30)           COMMENT '账户号',
    mch_no           VARCHAR(30)           COMMENT '商户号',
    agent_no         VARCHAR(30)           COMMENT '代理商号',
    sett_amount      BIGINT       NOT NULL COMMENT '结算金额(分)',
    fee_amount       BIGINT       NOT NULL DEFAULT 0 COMMENT '手续费(分)',
    remit_amount     BIGINT                COMMENT '打款金额(分)',
    state            TINYINT      NOT NULL DEFAULT 0 COMMENT '状态: 0-初始 1-结算中 2-成功 3-失败',
    bank_account_no  VARCHAR(30)           COMMENT '收款银行账号',
    bank_account_name VARCHAR(60)          COMMENT '收款人姓名',
    bank_name        VARCHAR(60)           COMMENT '收款银行',
    err_msg          VARCHAR(256)          COMMENT '错误描述',
    success_time     DATETIME              COMMENT '结算成功时间',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_sett_no (sett_no),
    INDEX idx_sett_mch (mch_no, state)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='结算记录表';

CREATE TABLE t_sett_bank_account (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    account_no       VARCHAR(30)  NOT NULL COMMENT '账户号(商户号/代理商号)',
    mch_no           VARCHAR(30)           COMMENT '商户号',
    bank_name        VARCHAR(60)           COMMENT '开户银行',
    bank_branch      VARCHAR(100)          COMMENT '开户支行',
    bank_account_no  VARCHAR(30)           COMMENT '银行账号',
    bank_account_name VARCHAR(60)          COMMENT '开户人姓名',
    account_type     TINYINT               COMMENT '账户类型: 1-对私 2-对公',
    state            TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-停用 1-正常',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_bank_acct (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='结算银行账户表';
