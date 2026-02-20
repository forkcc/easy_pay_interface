-- =============================================
-- 统计域 - 物化视图 (Statistics Materialized Views)
-- 替代传统预聚合表, 通过 REFRESH MATERIALIZED VIEW CONCURRENTLY
-- 定期刷新, 避免直接查询 2000 万级 t_pay_order
-- =============================================

-- 1) 每日订单统计 (按日期+商户+代理+支付方式)
CREATE MATERIALIZED VIEW mv_order_stat_daily AS
SELECT
    o.created_at::date                                        AS stat_date,
    o.mch_no,
    m.agent_no,
    COALESCE(o.way_code, '__ALL__')                           AS way_code,
    COUNT(*)                                                  AS order_count,
    COALESCE(SUM(o.amount), 0)                                AS order_amount,
    COALESCE(SUM(o.fee_amount), 0)                            AS fee_amount,
    COUNT(*) FILTER (WHERE o.state = 2)                       AS success_count,
    COALESCE(SUM(o.amount) FILTER (WHERE o.state = 2), 0)     AS success_amount
FROM t_pay_order o
LEFT JOIN t_mch_info m ON o.mch_no = m.mch_no
GROUP BY o.created_at::date, o.mch_no, m.agent_no, o.way_code
WITH DATA;

CREATE UNIQUE INDEX idx_mv_daily_stat
    ON mv_order_stat_daily (stat_date, mch_no, COALESCE(agent_no, ''), way_code);

COMMENT ON MATERIALIZED VIEW mv_order_stat_daily IS '每日订单统计物化视图(按日期+商户+代理+支付方式)';

-- 2) 商户汇总统计
CREATE MATERIALIZED VIEW mv_order_stat_mch AS
SELECT
    o.mch_no,
    m.agent_no,
    COUNT(*)                                                  AS total_count,
    COALESCE(SUM(o.amount), 0)                                AS total_amount,
    COALESCE(SUM(o.fee_amount), 0)                            AS total_fee,
    COUNT(*) FILTER (WHERE o.state = 2)                       AS success_count,
    COALESCE(SUM(o.amount) FILTER (WHERE o.state = 2), 0)     AS success_amount,
    MIN(o.created_at)                                         AS first_order_time,
    MAX(o.created_at)                                         AS last_order_time
FROM t_pay_order o
LEFT JOIN t_mch_info m ON o.mch_no = m.mch_no
GROUP BY o.mch_no, m.agent_no
WITH DATA;

CREATE UNIQUE INDEX idx_mv_stat_mch ON mv_order_stat_mch (mch_no);

COMMENT ON MATERIALIZED VIEW mv_order_stat_mch IS '商户级订单汇总物化视图';

-- 3) 支付方式汇总统计
CREATE MATERIALIZED VIEW mv_order_stat_way AS
SELECT
    way_code,
    COUNT(*)                                                  AS total_count,
    COALESCE(SUM(amount), 0)                                  AS total_amount,
    COALESCE(SUM(fee_amount), 0)                              AS total_fee,
    COUNT(*) FILTER (WHERE state = 2)                         AS success_count,
    COALESCE(SUM(amount) FILTER (WHERE state = 2), 0)         AS success_amount
FROM t_pay_order
WHERE way_code IS NOT NULL
GROUP BY way_code
WITH DATA;

CREATE UNIQUE INDEX idx_mv_stat_way ON mv_order_stat_way (way_code);

COMMENT ON MATERIALIZED VIEW mv_order_stat_way IS '支付方式级订单汇总物化视图';

-- 4) 每日全平台汇总 (用于运营总览)
CREATE MATERIALIZED VIEW mv_order_stat_platform_daily AS
SELECT
    created_at::date                                          AS stat_date,
    COUNT(*)                                                  AS order_count,
    COALESCE(SUM(amount), 0)                                  AS order_amount,
    COALESCE(SUM(fee_amount), 0)                              AS fee_amount,
    COUNT(*) FILTER (WHERE state = 2)                         AS success_count,
    COALESCE(SUM(amount) FILTER (WHERE state = 2), 0)         AS success_amount
FROM t_pay_order
GROUP BY created_at::date
WITH DATA;

CREATE UNIQUE INDEX idx_mv_platform_daily ON mv_order_stat_platform_daily (stat_date);

COMMENT ON MATERIALIZED VIEW mv_order_stat_platform_daily IS '平台每日汇总物化视图';
