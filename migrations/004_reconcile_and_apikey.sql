-- 对账汇总表、商户与代理 API Key（PostgreSQL）
CREATE TABLE IF NOT EXISTS reconcile_report (
    id              BIGSERIAL PRIMARY KEY,
    reconcile_date  DATE      NOT NULL,
    merchant_id     BIGINT    NOT NULL,
    pay_count       INTEGER   NOT NULL DEFAULT 0,
    pay_amount      BIGINT    NOT NULL DEFAULT 0,
    refund_count   INTEGER   NOT NULL DEFAULT 0,
    refund_amount   BIGINT    NOT NULL DEFAULT 0,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_date_merchant UNIQUE (reconcile_date, merchant_id)
);
CREATE INDEX IF NOT EXISTS idx_reconcile_report_date ON reconcile_report (reconcile_date);
CREATE INDEX IF NOT EXISTS idx_reconcile_report_merchant_id ON reconcile_report (merchant_id);
COMMENT ON TABLE reconcile_report IS '对账汇总表：按日、商户汇总支付/退款笔数与金额';

ALTER TABLE merchant ADD COLUMN IF NOT EXISTS api_key VARCHAR(64) DEFAULT NULL;
CREATE INDEX IF NOT EXISTS idx_merchant_api_key ON merchant (api_key);
COMMENT ON COLUMN merchant.api_key IS 'API Key，用于 X-Api-Key 鉴权';

ALTER TABLE agent ADD COLUMN IF NOT EXISTS api_key VARCHAR(64) DEFAULT NULL;
CREATE INDEX IF NOT EXISTS idx_agent_api_key ON agent (api_key);
COMMENT ON COLUMN agent.api_key IS 'API Key，用于 X-Api-Key 鉴权';
