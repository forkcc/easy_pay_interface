-- =============================================
-- 公共函数: updated_at 自动更新触发器
-- PostgreSQL 没有 ON UPDATE CURRENT_TIMESTAMP,
-- 通过触发器函数实现等效行为
-- =============================================

CREATE OR REPLACE FUNCTION update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
