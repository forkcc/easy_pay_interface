-- =============================================
-- 支付订单域 (Payment Order Domain)
-- t_pay_order: PostgreSQL 声明式按月 RANGE 分区, 支撑 2000 万级订单
-- t_refund_order, t_transfer_order
-- =============================================

-- 分区父表
CREATE TABLE t_pay_order (
    pay_order_id    VARCHAR(30)  NOT NULL,
    mch_no          VARCHAR(30)  NOT NULL,
    app_id          VARCHAR(30)  NOT NULL,
    mch_order_no    VARCHAR(30)  NOT NULL,
    way_code        VARCHAR(20),
    amount          BIGINT       NOT NULL,
    currency        VARCHAR(3)   NOT NULL DEFAULT 'CNY',
    state           SMALLINT     NOT NULL DEFAULT 0,
    client_ip       VARCHAR(45),
    subject         VARCHAR(128),
    body            VARCHAR(256),
    fee_rate        BIGINT,
    fee_amount      BIGINT,
    channel_order_no VARCHAR(64),
    channel_extra   TEXT,
    notify_url      VARCHAR(256),
    return_url      VARCHAR(256),
    err_code        VARCHAR(64),
    err_msg         VARCHAR(256),
    ext_param       TEXT,
    expired_time    TIMESTAMP,
    success_time    TIMESTAMP,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (pay_order_id, created_at)
) PARTITION BY RANGE (created_at);

CREATE INDEX idx_pay_order_mch_state ON t_pay_order (mch_no, state, created_at);
CREATE INDEX idx_pay_order_mch_order ON t_pay_order (mch_no, mch_order_no);
CREATE INDEX idx_pay_order_created   ON t_pay_order (created_at, state);
CREATE INDEX idx_pay_order_app       ON t_pay_order (app_id, state);

COMMENT ON TABLE  t_pay_order           IS '支付订单表(按月分区)';
COMMENT ON COLUMN t_pay_order.amount    IS '支付金额(分)';
COMMENT ON COLUMN t_pay_order.state     IS '0-初始 1-支付中 2-成功 3-失败 4-撤销 5-退款 6-关闭 7-过期';
COMMENT ON COLUMN t_pay_order.fee_rate  IS '费率(万分之)';
COMMENT ON COLUMN t_pay_order.fee_amount IS '手续费(分)';

-- 月分区 2026
CREATE TABLE t_pay_order_202601 PARTITION OF t_pay_order FOR VALUES FROM ('2026-01-01') TO ('2026-02-01');
CREATE TABLE t_pay_order_202602 PARTITION OF t_pay_order FOR VALUES FROM ('2026-02-01') TO ('2026-03-01');
CREATE TABLE t_pay_order_202603 PARTITION OF t_pay_order FOR VALUES FROM ('2026-03-01') TO ('2026-04-01');
CREATE TABLE t_pay_order_202604 PARTITION OF t_pay_order FOR VALUES FROM ('2026-04-01') TO ('2026-05-01');
CREATE TABLE t_pay_order_202605 PARTITION OF t_pay_order FOR VALUES FROM ('2026-05-01') TO ('2026-06-01');
CREATE TABLE t_pay_order_202606 PARTITION OF t_pay_order FOR VALUES FROM ('2026-06-01') TO ('2026-07-01');
CREATE TABLE t_pay_order_202607 PARTITION OF t_pay_order FOR VALUES FROM ('2026-07-01') TO ('2026-08-01');
CREATE TABLE t_pay_order_202608 PARTITION OF t_pay_order FOR VALUES FROM ('2026-08-01') TO ('2026-09-01');
CREATE TABLE t_pay_order_202609 PARTITION OF t_pay_order FOR VALUES FROM ('2026-09-01') TO ('2026-10-01');
CREATE TABLE t_pay_order_202610 PARTITION OF t_pay_order FOR VALUES FROM ('2026-10-01') TO ('2026-11-01');
CREATE TABLE t_pay_order_202611 PARTITION OF t_pay_order FOR VALUES FROM ('2026-11-01') TO ('2026-12-01');
CREATE TABLE t_pay_order_202612 PARTITION OF t_pay_order FOR VALUES FROM ('2026-12-01') TO ('2027-01-01');
CREATE TABLE t_pay_order_future PARTITION OF t_pay_order DEFAULT;

-- 退款订单
CREATE TABLE t_refund_order (
    refund_order_id  VARCHAR(30)  NOT NULL PRIMARY KEY,
    pay_order_id     VARCHAR(30)  NOT NULL,
    mch_no           VARCHAR(30)  NOT NULL,
    app_id           VARCHAR(30),
    mch_refund_no    VARCHAR(30),
    pay_amount       BIGINT,
    refund_amount    BIGINT       NOT NULL,
    currency         VARCHAR(3)   NOT NULL DEFAULT 'CNY',
    state            SMALLINT     NOT NULL DEFAULT 0,
    channel_order_no VARCHAR(64),
    channel_refund_no VARCHAR(64),
    reason           VARCHAR(256),
    err_code         VARCHAR(64),
    err_msg          VARCHAR(256),
    notify_url       VARCHAR(256),
    ext_param        TEXT,
    success_time     TIMESTAMP,
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_refund_pay_order ON t_refund_order (pay_order_id);
CREATE INDEX idx_refund_mch       ON t_refund_order (mch_no, state);
CREATE TRIGGER trg_refund_order_updated_at BEFORE UPDATE ON t_refund_order FOR EACH ROW EXECUTE FUNCTION update_updated_at();

COMMENT ON TABLE t_refund_order IS '退款订单表';

-- 转账订单
CREATE TABLE t_transfer_order (
    transfer_id      VARCHAR(30)  NOT NULL PRIMARY KEY,
    mch_no           VARCHAR(30)  NOT NULL,
    app_id           VARCHAR(30),
    mch_transfer_no  VARCHAR(30),
    way_code         VARCHAR(20),
    amount           BIGINT       NOT NULL,
    currency         VARCHAR(3)   NOT NULL DEFAULT 'CNY',
    state            SMALLINT     NOT NULL DEFAULT 0,
    account_no       VARCHAR(64),
    account_name     VARCHAR(64),
    bank_name        VARCHAR(64),
    channel_order_no VARCHAR(64),
    err_code         VARCHAR(64),
    err_msg          VARCHAR(256),
    ext_param        TEXT,
    success_time     TIMESTAMP,
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_transfer_mch ON t_transfer_order (mch_no, state);
CREATE TRIGGER trg_transfer_order_updated_at BEFORE UPDATE ON t_transfer_order FOR EACH ROW EXECUTE FUNCTION update_updated_at();

COMMENT ON TABLE t_transfer_order IS '转账订单表';
