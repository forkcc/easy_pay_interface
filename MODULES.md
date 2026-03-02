# 已实现模块信息

本仓库仅提供 **Dubbo 数据接口** 与 **Bot/Job**，无 HTTP API。各模块独立进程，通过环境变量 `DUBBO_PORT` 区分端口。

---

## 一、Dubbo 模块（仅 RPC 骨架）

### 1. 商户模块 (merchant)

| 项 | 说明 |
|----|------|
| 入口 | `go run ./cmd/merchant` |
| 默认端口 | 20001 |
| Dubbo 接口名 | `MerchantProvider` |
| 注册中心 | 可选 Zookeeper（`ZK_ADDR`） |
| 业务接口 | 已清除，仅保留 Provider 与启动逻辑 |

---

### 2. 代理模块 (agent)

| 项 | 说明 |
|----|------|
| 入口 | `go run ./cmd/agent` |
| 默认端口 | 20002 |
| Dubbo 接口名 | `AgentProvider` |
| 注册中心 | 可选 Zookeeper（`ZK_ADDR`） |
| 业务接口 | 已清除，仅保留 Provider 与启动逻辑 |

---

### 3. 支付模块 (payment)

| 项 | 说明 |
|----|------|
| 入口 | `go run ./cmd/payment` |
| 默认端口 | 20003 |
| Dubbo 接口名 | `PaymentProvider` |
| 注册中心 | 可选 Zookeeper（`ZK_ADDR`） |
| 业务接口 | 已清除，仅保留 Provider 与启动逻辑 |

---

## 二、非 Dubbo 模块

### 4. Telegram Bot (bot)

| 项 | 说明 |
|----|------|
| 入口 | `go run ./cmd/bot` |
| 说明 | Telegram 机器人，需配置 `TELEGRAM_BOT_TOKEN` |
| 协议 | 长轮询，无 HTTP 端口 |

---

### 5. 周期任务 (job)

| 项 | 说明 |
|----|------|
| 入口 | `go run ./cmd/job` |
| 说明 | 定时任务（当前为 5 分钟周期占位），可做通知重试、对账等 |
| 协议 | 无对外端口 |

---

## 三、汇总表

| 模块 | 入口命令 | 默认端口 | 协议/说明 |
|------|----------|----------|-----------|
| merchant | `./cmd/merchant` | 20001 | Dubbo，MerchantProvider（无业务方法） |
| agent | `./cmd/agent` | 20002 | Dubbo，AgentProvider（无业务方法） |
| payment | `./cmd/payment` | 20003 | Dubbo，PaymentProvider（无业务方法） |
| bot | `./cmd/bot` | - | Telegram 长轮询 |
| job | `./cmd/job` | - | 定时任务 |
