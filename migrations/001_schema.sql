-- 聚合支付平台表结构（PostgreSQL）
-- 执行顺序：按文件名 001～013

-- 001 商户表
CREATE TABLE IF NOT EXISTS merchant (
    id          BIGSERIAL PRIMARY KEY,
    merchant_no VARCHAR(32)  NOT NULL,
    name        VARCHAR(64)  NOT NULL,
    status      SMALLINT     NOT NULL DEFAULT 1,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_merchant_no UNIQUE (merchant_no)
);
COMMENT ON TABLE merchant IS '商户表';
COMMENT ON COLUMN merchant.status IS '状态：0-禁用 1-启用';

-- 002 代理表
CREATE TABLE IF NOT EXISTS agent (
    id          BIGSERIAL PRIMARY KEY,
    agent_no    VARCHAR(32)  NOT NULL,
    name        VARCHAR(64)  NOT NULL,
    parent_id   BIGINT                DEFAULT NULL,
    status      SMALLINT     NOT NULL DEFAULT 1,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_agent_no UNIQUE (agent_no)
);
CREATE INDEX IF NOT EXISTS idx_agent_parent_id ON agent (parent_id);
COMMENT ON TABLE agent IS '代理表';
COMMENT ON COLUMN agent.status IS '状态：0-禁用 1-启用';

-- 003 代理-商户关联表
CREATE TABLE IF NOT EXISTS agent_merchant (
    id          BIGSERIAL PRIMARY KEY,
    agent_id    BIGINT      NOT NULL,
    merchant_id BIGINT      NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_agent_merchant UNIQUE (agent_id, merchant_id)
);
CREATE INDEX IF NOT EXISTS idx_agent_merchant_merchant_id ON agent_merchant (merchant_id);
COMMENT ON TABLE agent_merchant IS '代理-商户关联表';

-- 004 支付接口表
CREATE TABLE IF NOT EXISTS payment_interface (
    id             BIGSERIAL PRIMARY KEY,
    interface_code VARCHAR(32) NOT NULL,
    name           VARCHAR(64) NOT NULL,
    request_tpl    TEXT,
    status         SMALLINT   NOT NULL DEFAULT 1,
    created_at      TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_interface_code UNIQUE (interface_code)
);
COMMENT ON TABLE payment_interface IS '支付接口表';
COMMENT ON COLUMN payment_interface.status IS '状态：0-禁用 1-启用';

-- 005 支付渠道表
CREATE TABLE IF NOT EXISTS payment_channel (
    id           BIGSERIAL PRIMARY KEY,
    channel_code VARCHAR(32)  NOT NULL,
    interface_id BIGINT       NOT NULL,
    params       TEXT,
    risk_config  TEXT,
    status       SMALLINT     NOT NULL DEFAULT 1,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_channel_code UNIQUE (channel_code)
);
CREATE INDEX IF NOT EXISTS idx_payment_channel_interface_id ON payment_channel (interface_id);
COMMENT ON TABLE payment_channel IS '支付渠道表';
COMMENT ON COLUMN payment_channel.status IS '状态：0-禁用 1-启用';

-- 006 支付产品表
CREATE TABLE IF NOT EXISTS payment_product (
    id           BIGSERIAL PRIMARY KEY,
    product_code VARCHAR(32)  NOT NULL,
    name         VARCHAR(64)  NOT NULL,
    status       SMALLINT     NOT NULL DEFAULT 1,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_product_code UNIQUE (product_code)
);
COMMENT ON TABLE payment_product IS '支付产品表';
COMMENT ON COLUMN payment_product.status IS '状态：0-禁用 1-启用';

-- 007 商户-支付产品关联表
CREATE TABLE IF NOT EXISTS merchant_product (
    id          BIGSERIAL PRIMARY KEY,
    merchant_id BIGINT    NOT NULL,
    product_id  BIGINT    NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_merchant_product UNIQUE (merchant_id, product_id)
);
CREATE INDEX IF NOT EXISTS idx_merchant_product_product_id ON merchant_product (product_id);
COMMENT ON TABLE merchant_product IS '商户-支付产品关联表';

-- 008 支付产品-渠道关联表
CREATE TABLE IF NOT EXISTS product_channel (
    id          BIGSERIAL PRIMARY KEY,
    product_id  BIGINT    NOT NULL,
    channel_id  BIGINT    NOT NULL,
    sort_order  INTEGER   NOT NULL DEFAULT 0,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_product_channel UNIQUE (product_id, channel_id)
);
CREATE INDEX IF NOT EXISTS idx_product_channel_channel_id ON product_channel (channel_id);
COMMENT ON TABLE product_channel IS '支付产品-渠道关联表';

-- 009 支付订单表
CREATE TABLE IF NOT EXISTS pay_order (
    id            BIGSERIAL PRIMARY KEY,
    pay_order_no  VARCHAR(64)  NOT NULL,
    merchant_id   BIGINT       NOT NULL,
    channel_id    BIGINT       NOT NULL,
    amount        BIGINT       NOT NULL,
    status        SMALLINT     NOT NULL DEFAULT 0,
    upstream_sn   VARCHAR(128)          DEFAULT NULL,
    notify_status SMALLINT     NOT NULL DEFAULT 0,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_pay_order_no UNIQUE (pay_order_no)
);
CREATE INDEX IF NOT EXISTS idx_pay_order_merchant_id ON pay_order (merchant_id);
CREATE INDEX IF NOT EXISTS idx_pay_order_channel_id ON pay_order (channel_id);
CREATE INDEX IF NOT EXISTS idx_pay_order_created_at ON pay_order (created_at);
COMMENT ON TABLE pay_order IS '支付订单表';
COMMENT ON COLUMN pay_order.status IS '状态：0-待支付 1-支付成功 2-支付失败 3-已关闭';
COMMENT ON COLUMN pay_order.notify_status IS '下游通知：0-未通知 1-已通知';

-- 010 退款订单表
CREATE TABLE IF NOT EXISTS refund_order (
    id              BIGSERIAL PRIMARY KEY,
    refund_order_no VARCHAR(64)  NOT NULL,
    pay_order_id    BIGINT       NOT NULL,
    merchant_id     BIGINT       NOT NULL,
    amount          BIGINT       NOT NULL,
    status          SMALLINT     NOT NULL DEFAULT 0,
    upstream_sn     VARCHAR(128)          DEFAULT NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_refund_order_no UNIQUE (refund_order_no)
);
CREATE INDEX IF NOT EXISTS idx_refund_order_pay_order_id ON refund_order (pay_order_id);
CREATE INDEX IF NOT EXISTS idx_refund_order_merchant_id ON refund_order (merchant_id);
COMMENT ON TABLE refund_order IS '退款订单表';
COMMENT ON COLUMN refund_order.status IS '状态：0-待退款 1-成功 2-失败';

-- 011 下游通知记录表
CREATE TABLE IF NOT EXISTS notify_log (
    id           BIGSERIAL PRIMARY KEY,
    biz_type     VARCHAR(16)  NOT NULL,
    biz_id       BIGINT       NOT NULL,
    notify_url   VARCHAR(512) NOT NULL,
    req_body     TEXT,
    resp_status  INTEGER               DEFAULT NULL,
    resp_body    TEXT,
    retry_count  INTEGER      NOT NULL DEFAULT 0,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_notify_log_biz ON notify_log (biz_type, biz_id);
CREATE INDEX IF NOT EXISTS idx_notify_log_created_at ON notify_log (created_at);
COMMENT ON TABLE notify_log IS '下游通知记录表';

-- 012 账户表
CREATE TABLE IF NOT EXISTS account (
    id          BIGSERIAL PRIMARY KEY,
    merchant_id BIGINT    NOT NULL,
    balance     BIGINT    NOT NULL DEFAULT 0,
    frozen      BIGINT    NOT NULL DEFAULT 0,
    version     INTEGER   NOT NULL DEFAULT 0,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_account_merchant_id UNIQUE (merchant_id)
);
COMMENT ON TABLE account IS '账户表';
COMMENT ON COLUMN account.balance IS '余额（分）';
COMMENT ON COLUMN account.frozen IS '冻结（分）';

-- 013 账户流水表
CREATE TABLE IF NOT EXISTS account_flow (
    id            BIGSERIAL PRIMARY KEY,
    account_id    BIGINT      NOT NULL,
    biz_type      VARCHAR(16) NOT NULL,
    biz_sn        VARCHAR(64) NOT NULL,
    amount        BIGINT      NOT NULL,
    balance_after BIGINT      NOT NULL,
    created_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_account_flow_account_id ON account_flow (account_id);
CREATE INDEX IF NOT EXISTS idx_account_flow_biz_sn ON account_flow (biz_sn);
CREATE INDEX IF NOT EXISTS idx_account_flow_created_at ON account_flow (created_at);
COMMENT ON TABLE account_flow IS '账户流水表';
