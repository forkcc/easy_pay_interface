# Dubbo RPC 接口说明（仿 Jeepay / 龙果支付）

各模块独立进程暴露 Dubbo 接口，供网关或其它服务调用。

## 商户模块 MerchantProvider

| 方法 | 说明 |
|------|------|
| GetByMchNo(ctx, mchNo string) (*entity.Merchant, error) | 按商户号查询商户（仿 Jeepay 商户信息） |
| GetByLoginName(ctx, loginName string) (*entity.Merchant, error) | 按登录名查询，用于登录校验 |

## 代理模块 AgentProvider

| 方法 | 说明 |
|------|------|
| GetByAgentNo(ctx, agentNo string) (*entity.Agent, error) | 按代理号查询代理 |
| GetByLoginName(ctx, loginName string) (*entity.Agent, error) | 按登录名查询，用于登录校验 |

## 管理端模块 ManagerProvider（仿 Jeepay / 龙果 管理后台）

| 方法 | 说明 |
|------|------|
| GetByLoginName(ctx, loginName string) (*entity.ManagerUser, error) | 按登录名查询管理端用户，用于登录校验 |
| GetByID(ctx, id int64) (*entity.ManagerUser, error) | 按主键查询管理端用户 |

## 支付模块 PaymentProvider（仿 Jeepay 支付网关 / 龙果支付）

| 方法 | 说明 |
|------|------|
| UnifiedOrder(ctx, req *UnifiedOrderReq) (*UnifiedOrderResp, error) | 统一下单（参数对齐 Jeepay 支付接口） |
| QueryOrder(ctx, payOrderNo string) (*entity.PayOrder, error) | 查询支付订单 |
| Refund(ctx, req *RefundReq) (*entity.RefundOrder, error) | 退款 |
| CloseOrder(ctx, payOrderNo string) error | 关闭未支付订单 |

### 统一下单参数 UnifiedOrderReq

- mchNo、appId、mchOrderNo、wayCode、amount 必填；currency 默认 CNY
- subject、body、notifyUrl、returnUrl、expiredSec 可选；expiredSec 为 0 时默认 2 小时

### 退款参数 RefundReq

- refundOrderNo、payOrderNo、mchNo、refundAmount 必填；reason 可选

## 启动与端口

- 商户：`go run ./cmd/merchant`，默认端口 20001，可通过 `DUBBO_PORT` 覆盖
- 代理：`go run ./cmd/agent`，默认 20002
- 支付：`go run ./cmd/payment`，默认 20003
- 管理端：`go run ./cmd/manager`，默认 20004

需配置环境变量 `DB_DSN`（PostgreSQL）；可选 `ZK_ADDR`（Zookeeper 注册中心）。
