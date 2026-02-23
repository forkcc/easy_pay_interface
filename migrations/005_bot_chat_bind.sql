-- Telegram Bot 群绑定表（PostgreSQL）
CREATE TABLE IF NOT EXISTS bot_chat_bind (
    id         BIGSERIAL PRIMARY KEY,
    chat_id    BIGINT      NOT NULL,
    bind_type  VARCHAR(16) NOT NULL,
    bind_id    VARCHAR(64) NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_bot_chat_bind_chat_id UNIQUE (chat_id)
);
CREATE INDEX IF NOT EXISTS idx_bot_chat_bind_bind ON bot_chat_bind (bind_type, bind_id);
COMMENT ON TABLE bot_chat_bind IS 'Bot 群绑定表：商户群、渠道群';
