-- 对账汇总按「创建时间」「更新时间」各一份（PostgreSQL）
ALTER TABLE reconcile_report ADD COLUMN IF NOT EXISTS time_type VARCHAR(16) NOT NULL DEFAULT 'created';

ALTER TABLE reconcile_report DROP CONSTRAINT IF EXISTS uk_date_merchant;
ALTER TABLE reconcile_report ADD CONSTRAINT uk_date_merchant_time UNIQUE (reconcile_date, merchant_id, time_type);

COMMENT ON COLUMN reconcile_report.time_type IS 'created=按订单创建时间, updated=按订单更新时间';
