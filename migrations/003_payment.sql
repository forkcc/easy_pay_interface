-- 支付（订单+退款+对账+结算）模块 | PostgreSQL schema: payment
-- 表：pay_type, pay_interface, pay_channel, pay_order, order_record, refund_order, reconcile_batch, reconcile_detail, settlement_record

CREATE SCHEMA IF NOT EXISTS payment;

CREATE TABLE IF NOT EXISTS payment.pay_type (
    id              BIGSERIAL PRIMARY KEY,
    type_code       VARCHAR(32) NOT NULL UNIQUE,
    type_name       VARCHAR(64) NOT NULL,
    sort_no         INT NOT NULL DEFAULT 0,
    state           SMALLINT NOT NULL DEFAULT 1,
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    updated_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT)
);
COMMENT ON TABLE payment.pay_type IS '支付类型（如 条码/扫码/H5）';
COMMENT ON COLUMN payment.pay_type.id IS '主键';
COMMENT ON COLUMN payment.pay_type.type_code IS '类型编码，唯一';
COMMENT ON COLUMN payment.pay_type.type_name IS '类型名称';
COMMENT ON COLUMN payment.pay_type.sort_no IS '排序号，升序';
COMMENT ON COLUMN payment.pay_type.state IS '状态：0 停用 1 启用';
COMMENT ON COLUMN payment.pay_type.created_at IS '创建时间，Unix 毫秒';
COMMENT ON COLUMN payment.pay_type.updated_at IS '更新时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_pay_type_code ON payment.pay_type(type_code);

CREATE TABLE IF NOT EXISTS payment.pay_interface (
    id              BIGSERIAL PRIMARY KEY,
    if_code         VARCHAR(32) NOT NULL UNIQUE,
    if_name         VARCHAR(64) NOT NULL,
    type_code       VARCHAR(32) NOT NULL,
    state           SMALLINT NOT NULL DEFAULT 1,
    remark          VARCHAR(256),
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    updated_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT)
);
COMMENT ON TABLE payment.pay_interface IS '支付接口（如 微信/支付宝）';
COMMENT ON COLUMN payment.pay_interface.id IS '主键';
COMMENT ON COLUMN payment.pay_interface.if_code IS '接口编码，唯一';
COMMENT ON COLUMN payment.pay_interface.if_name IS '接口名称';
COMMENT ON COLUMN payment.pay_interface.type_code IS '所属支付类型编码';
COMMENT ON COLUMN payment.pay_interface.state IS '状态：0 停用 1 启用';
COMMENT ON COLUMN payment.pay_interface.remark IS '备注';
COMMENT ON COLUMN payment.pay_interface.created_at IS '创建时间，Unix 毫秒';
COMMENT ON COLUMN payment.pay_interface.updated_at IS '更新时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_pay_interface_code ON payment.pay_interface(if_code);
CREATE INDEX IF NOT EXISTS idx_pay_interface_type ON payment.pay_interface(type_code);

CREATE TABLE IF NOT EXISTS payment.pay_channel (
    id              BIGSERIAL PRIMARY KEY,
    mch_no          VARCHAR(32) NOT NULL,
    app_id          VARCHAR(64) NOT NULL,
    if_code         VARCHAR(32) NOT NULL,
    way_code        VARCHAR(32) NOT NULL,
    channel_mch_no  VARCHAR(64),
    channel_mch_name VARCHAR(128),
    state           SMALLINT NOT NULL DEFAULT 1,
    config          JSONB,
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    updated_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    UNIQUE(mch_no, app_id, way_code)
);
COMMENT ON TABLE payment.pay_channel IS '支付渠道（商户+应用+支付方式维度配置）';
COMMENT ON COLUMN payment.pay_channel.id IS '主键';
COMMENT ON COLUMN payment.pay_channel.mch_no IS '商户号';
COMMENT ON COLUMN payment.pay_channel.app_id IS '应用 ID';
COMMENT ON COLUMN payment.pay_channel.if_code IS '支付接口编码';
COMMENT ON COLUMN payment.pay_channel.way_code IS '支付方式编码，如 wx_native、alipay_wap';
COMMENT ON COLUMN payment.pay_channel.channel_mch_no IS '渠道侧商户号';
COMMENT ON COLUMN payment.pay_channel.channel_mch_name IS '渠道侧商户名称';
COMMENT ON COLUMN payment.pay_channel.state IS '状态：0 停用 1 启用';
COMMENT ON COLUMN payment.pay_channel.config IS '渠道扩展参数，JSON';
COMMENT ON COLUMN payment.pay_channel.created_at IS '创建时间，Unix 毫秒';
COMMENT ON COLUMN payment.pay_channel.updated_at IS '更新时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_pay_channel_mch ON payment.pay_channel(mch_no);
CREATE INDEX IF NOT EXISTS idx_pay_channel_way ON payment.pay_channel(if_code, way_code);

CREATE TABLE IF NOT EXISTS payment.pay_order (
    id              BIGSERIAL PRIMARY KEY,
    pay_order_no    VARCHAR(64) NOT NULL UNIQUE,
    mch_no          VARCHAR(32) NOT NULL,
    app_id          VARCHAR(64) NOT NULL,
    if_code         VARCHAR(32) NOT NULL,
    way_code        VARCHAR(32) NOT NULL,
    amount          BIGINT NOT NULL,
    currency        VARCHAR(8) NOT NULL DEFAULT 'CNY',
    state           SMALLINT NOT NULL DEFAULT 0,
    channel_order_no VARCHAR(128),
    channel_uid     VARCHAR(128),
    subject         VARCHAR(256),
    body            VARCHAR(512),
    notify_url      VARCHAR(512),
    return_url      VARCHAR(512),
    expire_time     BIGINT,
    success_time    BIGINT,
    cancel_time     BIGINT,
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    updated_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT)
);
COMMENT ON TABLE payment.pay_order IS '支付订单';
COMMENT ON COLUMN payment.pay_order.id IS '主键';
COMMENT ON COLUMN payment.pay_order.pay_order_no IS '支付订单号，唯一';
COMMENT ON COLUMN payment.pay_order.mch_no IS '商户号';
COMMENT ON COLUMN payment.pay_order.app_id IS '应用 ID';
COMMENT ON COLUMN payment.pay_order.if_code IS '支付接口编码';
COMMENT ON COLUMN payment.pay_order.way_code IS '支付方式编码';
COMMENT ON COLUMN payment.pay_order.amount IS '订单金额，单位分';
COMMENT ON COLUMN payment.pay_order.currency IS '币种，默认 CNY';
COMMENT ON COLUMN payment.pay_order.state IS '订单状态：0 待支付 1 已支付 2 已关闭 3 已退款';
COMMENT ON COLUMN payment.pay_order.channel_order_no IS '渠道侧订单号';
COMMENT ON COLUMN payment.pay_order.channel_uid IS '渠道侧用户标识（如 openid）';
COMMENT ON COLUMN payment.pay_order.subject IS '订单标题';
COMMENT ON COLUMN payment.pay_order.body IS '订单描述';
COMMENT ON COLUMN payment.pay_order.notify_url IS '异步通知地址';
COMMENT ON COLUMN payment.pay_order.return_url IS '同步跳转地址';
COMMENT ON COLUMN payment.pay_order.expire_time IS '过期时间，Unix 毫秒';
COMMENT ON COLUMN payment.pay_order.success_time IS '支付成功时间，Unix 毫秒';
COMMENT ON COLUMN payment.pay_order.cancel_time IS '取消/关闭时间，Unix 毫秒';
COMMENT ON COLUMN payment.pay_order.created_at IS '创建时间，Unix 毫秒';
COMMENT ON COLUMN payment.pay_order.updated_at IS '更新时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_pay_order_no ON payment.pay_order(pay_order_no);
CREATE INDEX IF NOT EXISTS idx_pay_order_mch ON payment.pay_order(mch_no);
CREATE INDEX IF NOT EXISTS idx_pay_order_state ON payment.pay_order(state);
CREATE INDEX IF NOT EXISTS idx_pay_order_channel ON payment.pay_order(channel_order_no);
CREATE INDEX IF NOT EXISTS idx_pay_order_success_time ON payment.pay_order(success_time) WHERE success_time IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_pay_order_expire_time ON payment.pay_order(expire_time) WHERE expire_time IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_pay_order_created_at ON payment.pay_order(created_at);
CREATE INDEX IF NOT EXISTS idx_pay_order_updated_at ON payment.pay_order(updated_at);
CREATE INDEX IF NOT EXISTS idx_pay_order_cancel_time ON payment.pay_order(cancel_time) WHERE cancel_time IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_pay_order_mch_created ON payment.pay_order(mch_no, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_pay_order_mch_state ON payment.pay_order(mch_no, state);
CREATE INDEX IF NOT EXISTS idx_pay_order_state_expire ON payment.pay_order(state, expire_time) WHERE state = 0 AND expire_time IS NOT NULL;

CREATE TABLE IF NOT EXISTS payment.order_record (
    id              BIGSERIAL PRIMARY KEY,
    pay_order_no    VARCHAR(64) NOT NULL,
    event_type      VARCHAR(32) NOT NULL,
    old_state       SMALLINT,
    new_state       SMALLINT,
    extra           JSONB,
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT)
);
COMMENT ON TABLE payment.order_record IS '支付订单状态变更记录';
COMMENT ON COLUMN payment.order_record.id IS '主键';
COMMENT ON COLUMN payment.order_record.pay_order_no IS '支付订单号';
COMMENT ON COLUMN payment.order_record.event_type IS '事件类型：create / pay / close / refund / notify';
COMMENT ON COLUMN payment.order_record.old_state IS '变更前状态';
COMMENT ON COLUMN payment.order_record.new_state IS '变更后状态';
COMMENT ON COLUMN payment.order_record.extra IS '扩展信息，JSON';
COMMENT ON COLUMN payment.order_record.created_at IS '创建时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_order_record_pay_order ON payment.order_record(pay_order_no);
CREATE INDEX IF NOT EXISTS idx_order_record_created ON payment.order_record(created_at);
CREATE INDEX IF NOT EXISTS idx_order_record_pay_order_created ON payment.order_record(pay_order_no, created_at DESC);

-- 退款订单
CREATE TABLE IF NOT EXISTS payment.refund_order (
    id              BIGSERIAL PRIMARY KEY,
    refund_order_no VARCHAR(64) NOT NULL UNIQUE,
    pay_order_no    VARCHAR(64) NOT NULL,
    mch_no          VARCHAR(32) NOT NULL,
    app_id          VARCHAR(64) NOT NULL,
    refund_amount   BIGINT NOT NULL,
    currency        VARCHAR(8) NOT NULL DEFAULT 'CNY',
    state           SMALLINT NOT NULL DEFAULT 0,
    channel_refund_no VARCHAR(128),
    reason          VARCHAR(256),
    apply_time      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    success_time    BIGINT,
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    updated_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT)
);
COMMENT ON TABLE payment.refund_order IS '退款订单（独立退款单，便于对账与查询）';
COMMENT ON COLUMN payment.refund_order.id IS '主键';
COMMENT ON COLUMN payment.refund_order.refund_order_no IS '退款单号，唯一';
COMMENT ON COLUMN payment.refund_order.pay_order_no IS '原支付订单号';
COMMENT ON COLUMN payment.refund_order.mch_no IS '商户号';
COMMENT ON COLUMN payment.refund_order.app_id IS '应用 ID';
COMMENT ON COLUMN payment.refund_order.refund_amount IS '退款金额，单位分';
COMMENT ON COLUMN payment.refund_order.currency IS '币种，默认 CNY';
COMMENT ON COLUMN payment.refund_order.state IS '状态：0 申请中 1 成功 2 失败 3 已关闭';
COMMENT ON COLUMN payment.refund_order.channel_refund_no IS '渠道退款单号';
COMMENT ON COLUMN payment.refund_order.reason IS '退款原因';
COMMENT ON COLUMN payment.refund_order.apply_time IS '申请时间，Unix 毫秒';
COMMENT ON COLUMN payment.refund_order.success_time IS '渠道退款成功时间，Unix 毫秒';
COMMENT ON COLUMN payment.refund_order.created_at IS '创建时间，Unix 毫秒';
COMMENT ON COLUMN payment.refund_order.updated_at IS '更新时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_refund_order_no ON payment.refund_order(refund_order_no);
CREATE INDEX IF NOT EXISTS idx_refund_order_pay_order ON payment.refund_order(pay_order_no);
CREATE INDEX IF NOT EXISTS idx_refund_order_mch ON payment.refund_order(mch_no);
CREATE INDEX IF NOT EXISTS idx_refund_order_state ON payment.refund_order(state);
CREATE INDEX IF NOT EXISTS idx_refund_order_apply_time ON payment.refund_order(apply_time);
CREATE INDEX IF NOT EXISTS idx_refund_order_mch_apply ON payment.refund_order(mch_no, apply_time DESC);

-- 对账批次
CREATE TABLE IF NOT EXISTS payment.reconcile_batch (
    id              BIGSERIAL PRIMARY KEY,
    batch_no        VARCHAR(64) NOT NULL UNIQUE,
    channel         VARCHAR(32) NOT NULL,
    bill_date       BIGINT NOT NULL,
    state           SMALLINT NOT NULL DEFAULT 0,
    total_count     INT NOT NULL DEFAULT 0,
    match_count     INT NOT NULL DEFAULT 0,
    mistake_count   INT NOT NULL DEFAULT 0,
    remark          VARCHAR(256),
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    updated_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT)
);
COMMENT ON TABLE payment.reconcile_batch IS '对账批次（按渠道、账单日）';
COMMENT ON COLUMN payment.reconcile_batch.id IS '主键';
COMMENT ON COLUMN payment.reconcile_batch.batch_no IS '批次号，唯一';
COMMENT ON COLUMN payment.reconcile_batch.channel IS '渠道标识，如 if_code 或 渠道编码';
COMMENT ON COLUMN payment.reconcile_batch.bill_date IS '账单日期，Unix 毫秒或 YYYYMMDD 整数（按业务约定）';
COMMENT ON COLUMN payment.reconcile_batch.state IS '批次状态：0 处理中 1 已完成 2 异常';
COMMENT ON COLUMN payment.reconcile_batch.total_count IS '对账总笔数';
COMMENT ON COLUMN payment.reconcile_batch.match_count IS '一致笔数';
COMMENT ON COLUMN payment.reconcile_batch.mistake_count IS '差错笔数';
COMMENT ON COLUMN payment.reconcile_batch.remark IS '备注';
COMMENT ON COLUMN payment.reconcile_batch.created_at IS '创建时间，Unix 毫秒';
COMMENT ON COLUMN payment.reconcile_batch.updated_at IS '更新时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_reconcile_batch_no ON payment.reconcile_batch(batch_no);
CREATE INDEX IF NOT EXISTS idx_reconcile_batch_channel_date ON payment.reconcile_batch(channel, bill_date);
CREATE INDEX IF NOT EXISTS idx_reconcile_batch_state ON payment.reconcile_batch(state);
CREATE INDEX IF NOT EXISTS idx_reconcile_batch_created ON payment.reconcile_batch(created_at);

-- 对账明细/差错
CREATE TABLE IF NOT EXISTS payment.reconcile_detail (
    id              BIGSERIAL PRIMARY KEY,
    batch_no        VARCHAR(64) NOT NULL,
    pay_order_no    VARCHAR(64),
    channel_order_no VARCHAR(128),
    mch_no          VARCHAR(32),
    amount          BIGINT,
    pay_state       SMALLINT,
    channel_state   VARCHAR(32),
    diff_type       SMALLINT NOT NULL,
    diff_msg        VARCHAR(512),
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT)
);
COMMENT ON TABLE payment.reconcile_detail IS '对账明细（平台订单与渠道账单比对结果/差错记录）';
COMMENT ON COLUMN payment.reconcile_detail.id IS '主键';
COMMENT ON COLUMN payment.reconcile_detail.batch_no IS '所属批次号';
COMMENT ON COLUMN payment.reconcile_detail.pay_order_no IS '平台支付订单号';
COMMENT ON COLUMN payment.reconcile_detail.channel_order_no IS '渠道订单号';
COMMENT ON COLUMN payment.reconcile_detail.mch_no IS '商户号';
COMMENT ON COLUMN payment.reconcile_detail.amount IS '订单金额，单位分';
COMMENT ON COLUMN payment.reconcile_detail.pay_state IS '平台订单状态';
COMMENT ON COLUMN payment.reconcile_detail.channel_state IS '渠道订单状态或说明';
COMMENT ON COLUMN payment.reconcile_detail.diff_type IS '差异类型：0 一致 1 平台多 2 渠道多 3 金额不一致 4 状态不一致 等';
COMMENT ON COLUMN payment.reconcile_detail.diff_msg IS '差异说明';
COMMENT ON COLUMN payment.reconcile_detail.created_at IS '创建时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_reconcile_detail_batch ON payment.reconcile_detail(batch_no);
CREATE INDEX IF NOT EXISTS idx_reconcile_detail_pay_order ON payment.reconcile_detail(pay_order_no);
CREATE INDEX IF NOT EXISTS idx_reconcile_detail_channel_order ON payment.reconcile_detail(channel_order_no);
CREATE INDEX IF NOT EXISTS idx_reconcile_detail_diff_type ON payment.reconcile_detail(diff_type);
CREATE INDEX IF NOT EXISTS idx_reconcile_detail_mch ON payment.reconcile_detail(mch_no);

-- 结算记录
CREATE TABLE IF NOT EXISTS payment.settlement_record (
    id              BIGSERIAL PRIMARY KEY,
    settlement_no   VARCHAR(64) NOT NULL UNIQUE,
    acc_type        SMALLINT NOT NULL,
    acc_no          VARCHAR(32) NOT NULL,
    settle_period   VARCHAR(32) NOT NULL,
    settle_amount   BIGINT NOT NULL,
    fee_amount      BIGINT NOT NULL DEFAULT 0,
    state           SMALLINT NOT NULL DEFAULT 0,
    pay_time        BIGINT,
    pay_channel     VARCHAR(64),
    pay_trade_no    VARCHAR(128),
    remark          VARCHAR(256),
    created_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT),
    updated_at      BIGINT NOT NULL DEFAULT ((EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT)
);
COMMENT ON TABLE payment.settlement_record IS '结算记录（商户/代理结算与打款）';
COMMENT ON COLUMN payment.settlement_record.id IS '主键';
COMMENT ON COLUMN payment.settlement_record.settlement_no IS '结算单号，唯一';
COMMENT ON COLUMN payment.settlement_record.acc_type IS '账户类型：1 商户 2 代理';
COMMENT ON COLUMN payment.settlement_record.acc_no IS '商户号或代理号';
COMMENT ON COLUMN payment.settlement_record.settle_period IS '结算周期，如 20250301-20250331';
COMMENT ON COLUMN payment.settlement_record.settle_amount IS '结算金额，单位分';
COMMENT ON COLUMN payment.settlement_record.fee_amount IS '手续费，单位分';
COMMENT ON COLUMN payment.settlement_record.state IS '状态：0 待打款 1 已打款 2 失败';
COMMENT ON COLUMN payment.settlement_record.pay_time IS '打款时间，Unix 毫秒';
COMMENT ON COLUMN payment.settlement_record.pay_channel IS '打款渠道/方式';
COMMENT ON COLUMN payment.settlement_record.pay_trade_no IS '打款流水号';
COMMENT ON COLUMN payment.settlement_record.remark IS '备注';
COMMENT ON COLUMN payment.settlement_record.created_at IS '创建时间，Unix 毫秒';
COMMENT ON COLUMN payment.settlement_record.updated_at IS '更新时间，Unix 毫秒';
CREATE INDEX IF NOT EXISTS idx_settlement_no ON payment.settlement_record(settlement_no);
CREATE INDEX IF NOT EXISTS idx_settlement_acc ON payment.settlement_record(acc_type, acc_no);
CREATE INDEX IF NOT EXISTS idx_settlement_state ON payment.settlement_record(state);
CREATE INDEX IF NOT EXISTS idx_settlement_period ON payment.settlement_record(settle_period);
CREATE INDEX IF NOT EXISTS idx_settlement_created ON payment.settlement_record(created_at);
