-- 管理端模块 | PostgreSQL schema: manager
-- 表：manager.manager_user（商户/代理登录在各自主表内，账号密码 + Google OTP）

CREATE SCHEMA IF NOT EXISTS manager;

CREATE TABLE IF NOT EXISTS manager.manager_user (
    id              BIGSERIAL PRIMARY KEY,
    login_name      VARCHAR(64) NOT NULL UNIQUE,
    password_hash   VARCHAR(128) NOT NULL,
    otp_secret      VARCHAR(64),
    otp_enabled     SMALLINT NOT NULL DEFAULT 0,
    real_name       VARCHAR(32),
    state           SMALLINT NOT NULL DEFAULT 1,
    last_login_at   BIGINT,
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    updated_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT)
);
COMMENT ON TABLE manager.manager_user IS '管理端登录账号，含密码与 Google OTP';
COMMENT ON COLUMN manager.manager_user.id IS '主键';
COMMENT ON COLUMN manager.manager_user.login_name IS '登录名，唯一';
COMMENT ON COLUMN manager.manager_user.password_hash IS '密码哈希（如 bcrypt）';
COMMENT ON COLUMN manager.manager_user.otp_secret IS 'Google OTP 密钥';
COMMENT ON COLUMN manager.manager_user.otp_enabled IS 'OTP 是否已开启：0 未开启 1 已开启';
COMMENT ON COLUMN manager.manager_user.real_name IS '真实姓名';
COMMENT ON COLUMN manager.manager_user.state IS '状态：0 停用 1 启用';
COMMENT ON COLUMN manager.manager_user.last_login_at IS '最后登录时间，Unix 毫秒';
COMMENT ON COLUMN manager.manager_user.created_at IS '创建时间，Unix 毫秒';
COMMENT ON COLUMN manager.manager_user.updated_at IS '更新时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_manager_user_login ON manager.manager_user(login_name);
CREATE INDEX IF NOT EXISTS idx_manager_user_state ON manager.manager_user(state);
