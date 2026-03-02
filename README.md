# easy_pay_interface

支付相关 **Telegram Bot + 周期任务**。各模块独立运行。

## 技术栈

- Go 1.21+
- Telegram Bot API（go-telegram-bot-api/v5）
- RabbitMQ（amqp091-go）
- Redis（go-redis/v9）

## 目录结构（脚手架）

```
cmd/
  bot   Telegram Bot，独立进程
  job   周期任务（通知重试、对账等），独立进程
internal/
  config   配置（环境变量）
  rabbitmq RabbitMQ 连接与发布/消费
  redis    Redis 客户端
```

## 运行

```bash
# 依赖
go mod tidy

# 环境（可选）
export TELEGRAM_BOT_TOKEN="your_bot_token"   # bot 必填
export RABBITMQ_URL="amqp://guest:guest@localhost:5672/"
export REDIS_ADDR="localhost:6379"          # REDIS_PASSWORD、REDIS_DB 可选

# 各模块单独启动
go run ./cmd/bot   # Telegram Bot
go run ./cmd/job   # 周期任务
```

## 说明

- **bot**：Telegram 机器人，当前仅回复 "ok"，后续可接支付通知、查询等。
- **job**：定时任务（当前 5 分钟周期占位），后续可做通知重试、对账等。
