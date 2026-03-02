-- 插入默认管理员账户（执行 004_manager.sql 之后执行）
-- 登录名：admin  默认密码：password  首次登录后请修改密码并建议开启 OTP

INSERT INTO manager.manager_user (
    login_name,
    password_hash,
    otp_secret,
    otp_enabled,
    real_name,
    state,
    created_at,
    updated_at
) VALUES (
    'admin',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
    NULL,
    0,
    '系统管理员',
    1,
    (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT,
    (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT
)
ON CONFLICT (login_name) DO NOTHING;
