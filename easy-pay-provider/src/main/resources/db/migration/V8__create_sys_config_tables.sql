-- =============================================
-- 系统管理域 - 配置与日志 (System - Config & Log)
-- Tables: t_sys_config, t_sys_log
-- =============================================

CREATE TABLE t_sys_config (
    config_key       VARCHAR(60)  NOT NULL COMMENT '配置键',
    config_name      VARCHAR(60)           COMMENT '配置名称',
    config_value     TEXT                  COMMENT '配置值',
    config_group     VARCHAR(30)           COMMENT '分组: system-系统 settlement-结算 notify-通知 order-订单',
    remark           VARCHAR(256)          COMMENT '备注',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (config_key),
    INDEX idx_config_group (config_group)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

CREATE TABLE t_sys_log (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    user_type        TINYINT               COMMENT '用户类型: 1-运营 2-商户 3-代理商',
    user_id          VARCHAR(30)           COMMENT '用户ID',
    user_name        VARCHAR(30)           COMMENT '用户名',
    sys_type         VARCHAR(20)           COMMENT '系统类型: MGR-运营 MCH-商户 AGENT-代理商',
    method_name      VARCHAR(128)          COMMENT '方法名',
    method_remark    VARCHAR(128)          COMMENT '方法说明',
    req_url          VARCHAR(256)          COMMENT '请求URL',
    opt_req_param    TEXT                  COMMENT '请求参数(JSON)',
    opt_res_info     TEXT                  COMMENT '响应结果(JSON)',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_log_sys (sys_type, created_at),
    INDEX idx_log_user (sys_type, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';
