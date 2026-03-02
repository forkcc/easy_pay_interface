-- 商户模块 | PostgreSQL schema: merchant
-- 表：merchant.merchant（含登录账号密码+OTP）, merchant.merchant_fund_flow, merchant.merchant_callback_log

CREATE SCHEMA IF NOT EXISTS merchant;

CREATE TABLE IF NOT EXISTS merchant.merchant (
    id              BIGSERIAL PRIMARY KEY,
    mch_no          VARCHAR(32) NOT NULL UNIQUE,
    mch_name        VARCHAR(64) NOT NULL,
    login_name      VARCHAR(64) NOT NULL UNIQUE,
    password_hash   VARCHAR(128) NOT NULL,
    otp_secret      VARCHAR(64),
    otp_enabled     SMALLINT NOT NULL DEFAULT 0,
    contact_name    VARCHAR(32),
    contact_tel     VARCHAR(32),
    contact_email   VARCHAR(64),
    state           SMALLINT NOT NULL DEFAULT 1,
    balance         BIGINT NOT NULL DEFAULT 0,
    frozen_balance  BIGINT NOT NULL DEFAULT 0,
    last_login_at   BIGINT,
    remark          VARCHAR(256),
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    updated_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT)
);
COMMENT ON TABLE merchant.merchant IS '商户主表，含登录账号、密码哈希、Google OTP';
COMMENT ON COLUMN merchant.merchant.id IS '主键';
COMMENT ON COLUMN merchant.merchant.mch_no IS '商户号，唯一';
COMMENT ON COLUMN merchant.merchant.mch_name IS '商户名称';
COMMENT ON COLUMN merchant.merchant.login_name IS '登录名，唯一';
COMMENT ON COLUMN merchant.merchant.password_hash IS '密码哈希（如 bcrypt）';
COMMENT ON COLUMN merchant.merchant.otp_secret IS 'Google OTP 密钥';
COMMENT ON COLUMN merchant.merchant.otp_enabled IS 'OTP 是否已开启：0 未开启 1 已开启';
COMMENT ON COLUMN merchant.merchant.contact_name IS '联系人姓名';
COMMENT ON COLUMN merchant.merchant.contact_tel IS '联系人电话';
COMMENT ON COLUMN merchant.merchant.contact_email IS '联系人邮箱';
COMMENT ON COLUMN merchant.merchant.state IS '状态：0 停用 1 启用';
COMMENT ON COLUMN merchant.merchant.balance IS '可用余额，单位分';
COMMENT ON COLUMN merchant.merchant.frozen_balance IS '冻结余额，单位分';
COMMENT ON COLUMN merchant.merchant.last_login_at IS '最后登录时间，Unix 毫秒';
COMMENT ON COLUMN merchant.merchant.remark IS '备注';
COMMENT ON COLUMN merchant.merchant.created_at IS '创建时间，Unix 毫秒';
COMMENT ON COLUMN merchant.merchant.updated_at IS '更新时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_merchant_merchant_mch_no ON merchant.merchant(mch_no);
CREATE INDEX IF NOT EXISTS idx_merchant_merchant_login ON merchant.merchant(login_name);
CREATE INDEX IF NOT EXISTS idx_merchant_merchant_state ON merchant.merchant(state);

CREATE TABLE IF NOT EXISTS merchant.merchant_fund_flow (
    id              BIGSERIAL PRIMARY KEY,
    mch_no          VARCHAR(32) NOT NULL,
    flow_type       SMALLINT NOT NULL,
    amount          BIGINT NOT NULL,
    before_balance  BIGINT NOT NULL DEFAULT 0,
    after_balance   BIGINT NOT NULL DEFAULT 0,
    ref_type        VARCHAR(32),
    ref_no          VARCHAR(64),
    remark          VARCHAR(256),
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT)
);
COMMENT ON TABLE merchant.merchant_fund_flow IS '商户资金流水';
COMMENT ON COLUMN merchant.merchant_fund_flow.id IS '主键';
COMMENT ON COLUMN merchant.merchant_fund_flow.mch_no IS '商户号';
COMMENT ON COLUMN merchant.merchant_fund_flow.flow_type IS '流水类型：1 入账 2 出账 3 冻结 4 解冻';
COMMENT ON COLUMN merchant.merchant_fund_flow.amount IS '变动金额，单位分';
COMMENT ON COLUMN merchant.merchant_fund_flow.before_balance IS '变动前余额，单位分';
COMMENT ON COLUMN merchant.merchant_fund_flow.after_balance IS '变动后余额，单位分';
COMMENT ON COLUMN merchant.merchant_fund_flow.ref_type IS '关联业务类型，如 pay_order、withdraw';
COMMENT ON COLUMN merchant.merchant_fund_flow.ref_no IS '关联业务单号';
COMMENT ON COLUMN merchant.merchant_fund_flow.remark IS '备注';
COMMENT ON COLUMN merchant.merchant_fund_flow.created_at IS '创建时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_merchant_fund_flow_mch_no ON merchant.merchant_fund_flow(mch_no);
CREATE INDEX IF NOT EXISTS idx_merchant_fund_flow_ref ON merchant.merchant_fund_flow(ref_type, ref_no);
CREATE INDEX IF NOT EXISTS idx_merchant_fund_flow_created ON merchant.merchant_fund_flow(created_at);
CREATE INDEX IF NOT EXISTS idx_merchant_fund_flow_mch_created ON merchant.merchant_fund_flow(mch_no, created_at DESC);

CREATE TABLE IF NOT EXISTS merchant.merchant_callback_log (
    id              BIGSERIAL PRIMARY KEY,
    mch_no          VARCHAR(32) NOT NULL,
    pay_order_no    VARCHAR(64) NOT NULL,
    callback_url    VARCHAR(512) NOT NULL,
    callback_count  INT NOT NULL DEFAULT 0,
    response_code   INT,
    response_body   TEXT,
    state           SMALLINT NOT NULL DEFAULT 0,
    next_callback_at BIGINT,
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    updated_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT)
);
COMMENT ON TABLE merchant.merchant_callback_log IS '商户异步通知回调记录';
COMMENT ON COLUMN merchant.merchant_callback_log.id IS '主键';
COMMENT ON COLUMN merchant.merchant_callback_log.mch_no IS '商户号';
COMMENT ON COLUMN merchant.merchant_callback_log.pay_order_no IS '支付订单号';
COMMENT ON COLUMN merchant.merchant_callback_log.callback_url IS '回调地址';
COMMENT ON COLUMN merchant.merchant_callback_log.callback_count IS '已回调次数';
COMMENT ON COLUMN merchant.merchant_callback_log.response_code IS '最近一次 HTTP 状态码';
COMMENT ON COLUMN merchant.merchant_callback_log.response_body IS '最近一次响应内容';
COMMENT ON COLUMN merchant.merchant_callback_log.state IS '回调状态：0 待回调 1 成功 2 失败';
COMMENT ON COLUMN merchant.merchant_callback_log.next_callback_at IS '下次计划回调时间，Unix 毫秒';
COMMENT ON COLUMN merchant.merchant_callback_log.created_at IS '创建时间，Unix 毫秒';
COMMENT ON COLUMN merchant.merchant_callback_log.updated_at IS '更新时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_merchant_callback_mch ON merchant.merchant_callback_log(mch_no);
CREATE INDEX IF NOT EXISTS idx_merchant_callback_pay_order ON merchant.merchant_callback_log(pay_order_no);
CREATE INDEX IF NOT EXISTS idx_merchant_callback_state ON merchant.merchant_callback_log(state);
CREATE INDEX IF NOT EXISTS idx_merchant_callback_state_next ON merchant.merchant_callback_log(state, next_callback_at) WHERE state <> 1 AND next_callback_at IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_merchant_callback_mch_created ON merchant.merchant_callback_log(mch_no, created_at DESC);
