-- 分润规则表、提现申请表（PostgreSQL）
CREATE TABLE IF NOT EXISTS profit_share_rule (
    id          BIGSERIAL PRIMARY KEY,
    agent_id    BIGINT            DEFAULT NULL,
    merchant_id BIGINT            DEFAULT NULL,
    product_id  BIGINT            DEFAULT NULL,
    rate        INTEGER  NOT NULL DEFAULT 0,
    remark      VARCHAR(128)      DEFAULT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_profit_share_rule_agent_id ON profit_share_rule (agent_id);
CREATE INDEX IF NOT EXISTS idx_profit_share_rule_merchant_id ON profit_share_rule (merchant_id);
COMMENT ON TABLE profit_share_rule IS '分润规则表：代理/商户/产品维度分润比例（万分比，如 1000=10%）';

CREATE TABLE IF NOT EXISTS withdraw_order (
    id           BIGSERIAL PRIMARY KEY,
    merchant_id  BIGINT       NOT NULL,
    amount       BIGINT       NOT NULL,
    status       SMALLINT     NOT NULL DEFAULT 0,
    audit_remark VARCHAR(256)          DEFAULT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_withdraw_order_merchant_id ON withdraw_order (merchant_id);
CREATE INDEX IF NOT EXISTS idx_withdraw_order_created_at ON withdraw_order (created_at);
COMMENT ON TABLE withdraw_order IS '提现申请表';
COMMENT ON COLUMN withdraw_order.status IS '状态：0-待审核 1-已通过 2-已拒绝';
