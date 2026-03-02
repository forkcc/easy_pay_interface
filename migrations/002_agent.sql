-- 代理模块 | PostgreSQL schema: agent
-- 表：agent.agent（含登录账号密码+OTP）, agent.agent_fund_flow, agent.agent_merchant

CREATE SCHEMA IF NOT EXISTS agent;

CREATE TABLE IF NOT EXISTS agent.agent (
    id              BIGSERIAL PRIMARY KEY,
    agent_no        VARCHAR(32) NOT NULL UNIQUE,
    agent_name      VARCHAR(64) NOT NULL,
    login_name      VARCHAR(64) NOT NULL UNIQUE,
    password_hash   VARCHAR(128) NOT NULL,
    otp_secret      VARCHAR(64),
    otp_enabled     SMALLINT NOT NULL DEFAULT 0,
    contact_name    VARCHAR(32),
    contact_tel     VARCHAR(32),
    state           SMALLINT NOT NULL DEFAULT 1,
    balance         BIGINT NOT NULL DEFAULT 0,
    frozen_balance  BIGINT NOT NULL DEFAULT 0,
    last_login_at   BIGINT,
    remark          VARCHAR(256),
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    updated_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT)
);
COMMENT ON TABLE agent.agent IS '代理主表，含登录账号、密码哈希、Google OTP';
COMMENT ON COLUMN agent.agent.id IS '主键';
COMMENT ON COLUMN agent.agent.agent_no IS '代理号，唯一';
COMMENT ON COLUMN agent.agent.agent_name IS '代理名称';
COMMENT ON COLUMN agent.agent.login_name IS '登录名，唯一';
COMMENT ON COLUMN agent.agent.password_hash IS '密码哈希（如 bcrypt）';
COMMENT ON COLUMN agent.agent.otp_secret IS 'Google OTP 密钥';
COMMENT ON COLUMN agent.agent.otp_enabled IS 'OTP 是否已开启：0 未开启 1 已开启';
COMMENT ON COLUMN agent.agent.contact_name IS '联系人姓名';
COMMENT ON COLUMN agent.agent.contact_tel IS '联系人电话';
COMMENT ON COLUMN agent.agent.state IS '状态：0 停用 1 启用';
COMMENT ON COLUMN agent.agent.balance IS '可用余额，单位分';
COMMENT ON COLUMN agent.agent.frozen_balance IS '冻结余额，单位分';
COMMENT ON COLUMN agent.agent.last_login_at IS '最后登录时间，Unix 毫秒';
COMMENT ON COLUMN agent.agent.remark IS '备注';
COMMENT ON COLUMN agent.agent.created_at IS '创建时间，Unix 毫秒';
COMMENT ON COLUMN agent.agent.updated_at IS '更新时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_agent_agent_no ON agent.agent(agent_no);
CREATE INDEX IF NOT EXISTS idx_agent_agent_login ON agent.agent(login_name);
CREATE INDEX IF NOT EXISTS idx_agent_agent_state ON agent.agent(state);

CREATE TABLE IF NOT EXISTS agent.agent_fund_flow (
    id              BIGSERIAL PRIMARY KEY,
    agent_no        VARCHAR(32) NOT NULL,
    flow_type       SMALLINT NOT NULL,
    amount          BIGINT NOT NULL,
    before_balance  BIGINT NOT NULL DEFAULT 0,
    after_balance   BIGINT NOT NULL DEFAULT 0,
    ref_type        VARCHAR(32),
    ref_no          VARCHAR(64),
    remark          VARCHAR(256),
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT)
);
COMMENT ON TABLE agent.agent_fund_flow IS '代理资金流水';
COMMENT ON COLUMN agent.agent_fund_flow.id IS '主键';
COMMENT ON COLUMN agent.agent_fund_flow.agent_no IS '代理号';
COMMENT ON COLUMN agent.agent_fund_flow.flow_type IS '流水类型：1 入账 2 出账 3 冻结 4 解冻';
COMMENT ON COLUMN agent.agent_fund_flow.amount IS '变动金额，单位分';
COMMENT ON COLUMN agent.agent_fund_flow.before_balance IS '变动前余额，单位分';
COMMENT ON COLUMN agent.agent_fund_flow.after_balance IS '变动后余额，单位分';
COMMENT ON COLUMN agent.agent_fund_flow.ref_type IS '关联业务类型';
COMMENT ON COLUMN agent.agent_fund_flow.ref_no IS '关联业务单号';
COMMENT ON COLUMN agent.agent_fund_flow.remark IS '备注';
COMMENT ON COLUMN agent.agent_fund_flow.created_at IS '创建时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_agent_fund_flow_agent_no ON agent.agent_fund_flow(agent_no);
CREATE INDEX IF NOT EXISTS idx_agent_fund_flow_ref ON agent.agent_fund_flow(ref_type, ref_no);
CREATE INDEX IF NOT EXISTS idx_agent_fund_flow_created ON agent.agent_fund_flow(created_at);
CREATE INDEX IF NOT EXISTS idx_agent_fund_flow_agent_created ON agent.agent_fund_flow(agent_no, created_at DESC);

CREATE TABLE IF NOT EXISTS agent.agent_merchant (
    id              BIGSERIAL PRIMARY KEY,
    agent_no        VARCHAR(32) NOT NULL,
    mch_no          VARCHAR(32) NOT NULL,
    bind_time       BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    state           SMALLINT NOT NULL DEFAULT 1,
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    updated_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    UNIQUE(agent_no, mch_no)
);
COMMENT ON TABLE agent.agent_merchant IS '代理与商户绑定关系';
COMMENT ON COLUMN agent.agent_merchant.id IS '主键';
COMMENT ON COLUMN agent.agent_merchant.agent_no IS '代理号';
COMMENT ON COLUMN agent.agent_merchant.mch_no IS '商户号';
COMMENT ON COLUMN agent.agent_merchant.bind_time IS '绑定时间，Unix 毫秒';
COMMENT ON COLUMN agent.agent_merchant.state IS '状态：0 解绑 1 绑定';
COMMENT ON COLUMN agent.agent_merchant.created_at IS '创建时间，Unix 毫秒';
COMMENT ON COLUMN agent.agent_merchant.updated_at IS '更新时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_agent_merchant_agent ON agent.agent_merchant(agent_no);
CREATE INDEX IF NOT EXISTS idx_agent_merchant_mch ON agent.agent_merchant(mch_no);
