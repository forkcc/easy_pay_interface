-- =============================================
-- 数据统计域 (Statistics Domain)
-- Tables: t_order_stat_daily
-- 每日订单预聚合统计表，避免直接查询 2000 万级 t_pay_order
-- =============================================

CREATE TABLE t_order_stat_daily (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    stat_date        DATE         NOT NULL COMMENT '统计日期',
    mch_no           VARCHAR(30)           COMMENT '商户号',
    agent_no         VARCHAR(30)           COMMENT '代理商号',
    way_code         VARCHAR(20)           COMMENT '支付方式代码',
    passage_id       BIGINT                COMMENT '通道ID',
    order_count      INT          NOT NULL DEFAULT 0 COMMENT '订单总笔数',
    order_amount     BIGINT       NOT NULL DEFAULT 0 COMMENT '订单总金额(分)',
    fee_amount       BIGINT       NOT NULL DEFAULT 0 COMMENT '手续费总额(分)',
    success_count    INT          NOT NULL DEFAULT 0 COMMENT '成功笔数',
    success_amount   BIGINT       NOT NULL DEFAULT 0 COMMENT '成功金额(分)',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_daily_stat (stat_date, mch_no, agent_no, way_code, passage_id),
    INDEX idx_stat_date (stat_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日订单统计预聚合表';
