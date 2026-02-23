-- 商户通知地址、下单/退款幂等字段（PostgreSQL）
ALTER TABLE merchant ADD COLUMN IF NOT EXISTS notify_url VARCHAR(512) DEFAULT NULL;
COMMENT ON COLUMN merchant.notify_url IS '异步通知地址';

ALTER TABLE pay_order ADD COLUMN IF NOT EXISTS out_trade_no VARCHAR(64) DEFAULT NULL;
ALTER TABLE pay_order ADD CONSTRAINT uk_merchant_out_trade_no UNIQUE (merchant_id, out_trade_no);
COMMENT ON COLUMN pay_order.out_trade_no IS '商户订单号';

ALTER TABLE refund_order ADD COLUMN IF NOT EXISTS out_refund_no VARCHAR(64) DEFAULT NULL;
ALTER TABLE refund_order ADD CONSTRAINT uk_merchant_out_refund_no UNIQUE (merchant_id, out_refund_no);
COMMENT ON COLUMN refund_order.out_refund_no IS '商户退款单号';
