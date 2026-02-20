-- =============================================
-- 系统基础数据初始化
-- 系统配置 + 超级管理员 + 角色
-- =============================================

-- 系统参数配置
INSERT INTO t_sys_config (config_key, config_name, config_value, config_group, remark) VALUES
('siteName',         '站点名称',            'Easy Pay',         'system',     '平台名称'),
('siteUrl',          '站点地址',            'http://localhost',  'system',     '平台首页地址'),
('ossType',          '存储类型',            'local',            'system',     '文件存储类型: local-本地 oss-阿里云 cos-腾讯云'),
('uploadMaxSize',    '上传文件大小限制(MB)',  '10',               'system',     '上传文件的最大体积(MB)');

-- 结算参数配置
INSERT INTO t_sys_config (config_key, config_name, config_value, config_group, remark) VALUES
('settleCycle',      '结算周期(天)',         '1',                'settlement', 'T+N 结算, N值'),
('settleMinAmount',  '最低结算金额(分)',      '10000',            'settlement', '低于此金额不可发起结算, 单位分'),
('settleFeeRate',    '结算手续费率(万分之)',   '0',                'settlement', '结算手续费费率, 万分之'),
('settleFeeMin',     '结算手续费下限(分)',     '0',                'settlement', '单笔结算最低手续费, 单位分');

-- 通知参数配置
INSERT INTO t_sys_config (config_key, config_name, config_value, config_group, remark) VALUES
('notifyMaxCount',   '通知最大次数',         '6',                'notify',     '支付结果异步通知最大重试次数'),
('notifyInterval',   '通知间隔(秒)',         '15,15,30,180,1800,86400', 'notify', '每次通知的间隔时间(秒), 逗号分隔');

-- 订单参数配置
INSERT INTO t_sys_config (config_key, config_name, config_value, config_group, remark) VALUES
('orderExpireMin',   '订单过期时间(分钟)',    '30',               'order',      '未支付订单自动过期的时间'),
('refundExpireDay',  '退款有效期(天)',        '90',               'order',      '订单支付后可退款的最大天数');

-- 超级管理员用户 (密码: admin123, 首次登录后应立即修改)
-- BCrypt hash of 'admin123'
INSERT INTO t_sys_user (login_username, login_password, realname, user_type, belong_id, state)
VALUES ('admin', '$2a$10$VGk9bHELJxQT/7ERaWJSpOPIqLmBqLHn6BcjYMbOrdoL1.WdKIJGy', '超级管理员', 1, '0', 1);

-- 超级管理员角色
INSERT INTO t_sys_role (role_id, role_name, belong_type)
VALUES ('ROLE_ADMIN', '超级管理员', 'MGR');

-- 关联管理员用户与角色
INSERT INTO t_sys_user_role (sys_user_id, role_id)
VALUES (1, 'ROLE_ADMIN');
