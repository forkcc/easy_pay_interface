# 聚合支付平台（Go）

聚合支付 API：管理端、代理端、商户端、支付端四端接口。

## 环境

- Go 1.21+
- PostgreSQL 12+（库名 `easy_pay`）

## 目录

```
cmd/app       主 HTTP 服务（四端路由）
cmd/bot       Telegram Bot（商户群/渠道群/超级用户）
cmd/job       定时任务（通知重试、对账）
internal/
  app         路由与 DB 初始化
  bot         Bot 配置与长轮询
  entity      表实体
  repository  数据访问
  handler/    各端 handler（manager/agent/merchant/payment）
  response    统一响应 R、PageResult
migrations/   建表与变更 SQL（001～007，PostgreSQL）
```

## 运行

```bash
go mod tidy

# 建库后按顺序执行 migrations/001_schema.sql ～ 007_admin_agent_audit.sql

# 启动 API（默认 :8080）
export DB_DSN="host=localhost user=postgres password=postgres dbname=easy_pay port=5432 sslmode=disable TimeZone=Asia/Shanghai"
export JWT_SECRET="your-secret"   # 可选，生产环境务必设置
go run ./cmd/app

# 首次使用：创建管理员（仅当尚无任何管理员时可调用）
# curl -X POST http://localhost:8080/manager/init -H "Content-Type: application/json" -d '{"username":"admin","password":"xxx","name":"管理员"}'

# 管理端登录：POST /manager/login {"username","password"} -> 返回 token，后续请求 Header: Authorization: Bearer <token>
# 代理端登录：POST /agent/login {"agentNo","username","password"} -> 返回 token（代理账号由管理端 POST /manager/agent/:id/user 创建）
# 商户端/支付端：请求头必须带 X-Api-Key 或 X-Merchant-Id，否则 401

# 指定端口
HTTP_ADDR=:9000 go run ./cmd/app

# 定时任务（独立进程）
go run ./cmd/job

# Telegram Bot（可选）
BOT_TOKEN=xxx BOT_SUPER_ACCOUNTS=admin go run ./cmd/bot
```

## 接口前缀

| 端   | 前缀       |
|------|------------|
| 管理端 | `/manager`  |
| 代理端 | `/agent`    |
| 商户端 | `/merchant` |
| 支付端 | `/payment`  |

示例：`GET /manager/merchant/page`、`GET /payment/order/query?payOrderNo=xxx`。

## 表结构

按顺序执行 `migrations/001_schema.sql` 至 `007_admin_agent_audit.sql`（PostgreSQL）创建全部表（含 admin_user、agent_user、audit_log 及 api_key 唯一约束）。
