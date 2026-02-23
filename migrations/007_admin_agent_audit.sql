-- 管理端账号、代理端账号、操作审计、apiKey 唯一约束（PostgreSQL）

-- 管理端账号表
CREATE TABLE IF NOT EXISTS admin_user (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(64)  NOT NULL,
    password_hash VARCHAR(128) NOT NULL,
    name          VARCHAR(64)  NOT NULL DEFAULT '',
    status        SMALLINT     NOT NULL DEFAULT 1,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_admin_user_username UNIQUE (username)
);
COMMENT ON TABLE admin_user IS '管理端登录账号';
CREATE INDEX IF NOT EXISTS idx_admin_user_status ON admin_user (status);

-- 代理端账号表（绑定 agent_id）
CREATE TABLE IF NOT EXISTS agent_user (
    id            BIGSERIAL PRIMARY KEY,
    agent_id      BIGINT       NOT NULL,
    username      VARCHAR(64)  NOT NULL,
    password_hash VARCHAR(128) NOT NULL,
    name          VARCHAR(64)  NOT NULL DEFAULT '',
    status        SMALLINT     NOT NULL DEFAULT 1,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_agent_user_agent_username UNIQUE (agent_id, username)
);
COMMENT ON TABLE agent_user IS '代理端登录账号';
CREATE INDEX IF NOT EXISTS idx_agent_user_agent_id ON agent_user (agent_id);
CREATE INDEX IF NOT EXISTS idx_agent_user_status ON agent_user (status);

-- 操作审计表
CREATE TABLE IF NOT EXISTS audit_log (
    id          BIGSERIAL PRIMARY KEY,
    user_type   VARCHAR(16)  NOT NULL,
    user_id     BIGINT       NOT NULL,
    username    VARCHAR(64)  NOT NULL DEFAULT '',
    action      VARCHAR(64)  NOT NULL,
    target_type VARCHAR(32)  NOT NULL DEFAULT '',
    target_id   VARCHAR(64)  NOT NULL DEFAULT '',
    detail      JSONB,
    ip          VARCHAR(64)  NOT NULL DEFAULT '',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE audit_log IS '操作审计日志';
CREATE INDEX IF NOT EXISTS idx_audit_log_user_type_id ON audit_log (user_type, user_id);
CREATE INDEX IF NOT EXISTS idx_audit_log_action ON audit_log (action);
CREATE INDEX IF NOT EXISTS idx_audit_log_created_at ON audit_log (created_at);

-- 商户/代理 api_key 非空时唯一（PostgreSQL 部分唯一索引）
CREATE UNIQUE INDEX IF NOT EXISTS idx_merchant_api_key_unique ON merchant (api_key) WHERE api_key IS NOT NULL AND api_key != '';
CREATE UNIQUE INDEX IF NOT EXISTS idx_agent_api_key_unique ON agent (api_key) WHERE api_key IS NOT NULL AND api_key != '';
