# easy_pay_interface

支付相关 **Dubbo 数据接口**（商户 + 代理 + 支付）+ **Telegram Bot + 周期任务**。参考 [Jeepay](https://github.com/jeequan/jeepay)、[龙果支付 roncoo-pay](https://github.com/roncoo/roncoo-pay)，**仅暴露 Dubbo 接口，不提供 HTTP API**；各模块独立进程运行。

## 技术栈

- Go 1.21+
- Apache Dubbo-Go（Dubbo 协议，可选 Zookeeper 注册）
- Telegram Bot API（go-telegram-bot-api/v5）
- RabbitMQ（amqp091-go）、Redis（go-redis/v9）

**模块一览**：详见 [MODULES.md](./MODULES.md)（入口、端口、Dubbo 接口与方法列表）。

## 目录结构

```
cmd/
  merchant  商户模块 Dubbo 服务（默认端口 20001）
  agent     代理模块 Dubbo 服务（默认端口 20002）
  payment   支付模块 Dubbo 服务（默认端口 20003）
  bot       Telegram Bot
  job       周期任务（通知重试、对账等）
internal/
  config    配置（环境变量）
  rpc       Dubbo 服务实现（MerchantProvider / AgentProvider / PaymentProvider）
  rabbitmq  RabbitMQ 连接
  redis     Redis 客户端
```

## Dubbo 接口说明

商户 / 代理 / 支付三个模块仅保留 Provider 骨架与启动逻辑，**当前未实现业务 RPC 方法**。后续可在各 Provider 上增加方法并接 DB/Redis/MQ。

## 运行

```bash
go mod tidy

# 环境（可选）
export ZK_ADDR="127.0.0.1:2181"              # Dubbo 注册到 Zookeeper，为空则不注册
export TELEGRAM_BOT_TOKEN="your_bot_token"   # bot 必填
export RABBITMQ_URL="amqp://guest:guest@localhost:5672/"
export REDIS_ADDR="localhost:6379"

# 各模块单独启动（多机部署时每台设不同 DUBBO_PORT）
go run ./cmd/merchant   # 商户 Dubbo，默认 :20001
go run ./cmd/agent     # 代理 Dubbo，默认 :20002
go run ./cmd/payment    # 支付 Dubbo，默认 :20003
go run ./cmd/bot        # Telegram Bot
go run ./cmd/job        # 周期任务
```

通过 `DUBBO_PORT` 可覆盖默认端口（如单机多实例需不同端口）。

## Docker Compose（host 网络）

`docker-compose.yaml` 仅包含应用服务（merchant / agent / payment / bot / job），使用 `network_mode: host`。**Zookeeper、Postgres、Redis、RabbitMQ 不写在 compose 内**，需在宿主机或其它环境单独部署，通过环境变量连接。

```bash
export ZK_ADDR=127.0.0.1:2181
export TELEGRAM_BOT_TOKEN=your_bot_token   # bot 必填
docker compose up -d
```

## 说明

- **商户 / 代理 / 支付**：仅提供 Dubbo RPC，业务 API 由调用方项目提供；当前为占位实现，返回 JSON，后续可接 DB、Redis、RabbitMQ 实现真实逻辑。
- **bot / job**：Telegram 机器人与定时任务，与上述三模块无强耦合。
