# 表关系说明

当前迁移脚本**未使用外键约束**（跨 schema、先插数据后建表顺序更灵活），以下为**逻辑上的表关系**与**字段引用一致性**检查结果。

---

## 一、关系概览

```
merchant.merchant (mch_no)
    ├── merchant.merchant_fund_flow.mch_no
    ├── merchant.merchant_callback_log.mch_no
    ├── agent.agent_merchant.mch_no
    ├── payment.pay_channel.mch_no
    ├── payment.pay_order.mch_no
    ├── payment.refund_order.mch_no
    ├── payment.reconcile_detail.mch_no
    └── payment.settlement_record.acc_no (当 acc_type=1)

agent.agent (agent_no)
    ├── agent.agent_fund_flow.agent_no
    ├── agent.agent_merchant.agent_no
    └── payment.settlement_record.acc_no (当 acc_type=2)

payment.pay_type (type_code)
    └── payment.pay_interface.type_code

payment.pay_interface (if_code)
    ├── payment.pay_channel.if_code
    └── payment.pay_order.if_code

payment.pay_order (pay_order_no)
    ├── payment.order_record.pay_order_no
    ├── payment.refund_order.pay_order_no
    ├── merchant.merchant_callback_log.pay_order_no
    └── payment.reconcile_detail.pay_order_no

payment.reconcile_batch (batch_no)
    └── payment.reconcile_detail.batch_no
```

---

## 二、按模块的引用关系

### 1. merchant 模块

| 表 | 引用外部 | 被引用 |
|----|----------|--------|
| merchant.merchant | — | mch_no → merchant_fund_flow, merchant_callback_log；且被 agent_merchant / pay_channel / pay_order / refund_order / reconcile_detail / settlement_record 使用 |
| merchant.merchant_fund_flow | mch_no → merchant.merchant；ref_type/ref_no → 业务单（如 pay_order、refund_order、settlement） | — |
| merchant.merchant_callback_log | mch_no → merchant.merchant；pay_order_no → payment.pay_order | — |

### 2. agent 模块

| 表 | 引用外部 | 被引用 |
|----|----------|--------|
| agent.agent | — | agent_no → agent_fund_flow, agent_merchant；settlement_record(acc_type=2) 的 acc_no |
| agent.agent_fund_flow | agent_no → agent.agent；ref_type/ref_no → 业务单 | — |
| agent.agent_merchant | agent_no → agent.agent；mch_no → merchant.merchant | — |

### 3. payment 模块

| 表 | 引用外部 | 被引用 |
|----|----------|--------|
| pay_type | — | type_code → pay_interface |
| pay_interface | type_code → pay_type | if_code → pay_channel, pay_order |
| pay_channel | mch_no→merchant；if_code→pay_interface；app_id 无主表（建议 mch_app） | — |
| pay_order | mch_no→merchant；if_code→pay_interface；app_id 无主表 | pay_order_no → order_record, refund_order, merchant_callback_log, reconcile_detail |
| order_record | pay_order_no → pay_order | — |
| refund_order | pay_order_no → pay_order；mch_no → merchant；app_id 无主表 | — |
| reconcile_batch | channel 可与 if_code 对应 | batch_no → reconcile_detail |
| reconcile_detail | batch_no → reconcile_batch；pay_order_no→pay_order；mch_no→merchant | — |
| settlement_record | acc_no + acc_type：1→merchant.mch_no，2→agent.agent_no | — |

### 4. manager 模块

| 表 | 引用外部 | 被引用 |
|----|----------|--------|
| manager.manager_user | — | 无其他表引用 |

---

## 三、字段长度与类型一致性

| 关联 | 主/源 | 从表/字段 | 是否一致 |
|------|--------|-----------|----------|
| 商户号 | merchant.merchant.mch_no VARCHAR(32) | 各表 mch_no 均为 VARCHAR(32) | ✓ |
| 代理号 | agent.agent.agent_no VARCHAR(32) | agent_fund_flow.agent_no, agent_merchant.agent_no, settlement_record.acc_no 均为 VARCHAR(32) | ✓ |
| 支付订单号 | payment.pay_order.pay_order_no VARCHAR(64) | order_record, refund_order, merchant_callback_log, reconcile_detail 均为 VARCHAR(64) | ✓ |
| 退款单号 | payment.refund_order.refund_order_no VARCHAR(64) | merchant_fund_flow.ref_no 可存 VARCHAR(64) | ✓ |
| 结算单号 | payment.settlement_record.settlement_no VARCHAR(64) | merchant_fund_flow.ref_no 可存 VARCHAR(64) | ✓ |
| 对账批次号 | payment.reconcile_batch.batch_no VARCHAR(64) | reconcile_detail.batch_no VARCHAR(64) | ✓ |
| 支付类型编码 | pay_type.type_code VARCHAR(32) | pay_interface.type_code VARCHAR(32) | ✓ |
| 支付接口编码 | pay_interface.if_code VARCHAR(32) | pay_channel.if_code, pay_order.if_code VARCHAR(32)；reconcile_batch.channel VARCHAR(32) | ✓ |
| 应用 ID | — | pay_channel.app_id, pay_order.app_id, refund_order.app_id 均为 VARCHAR(64) | ✓（暂无 mch_app 主表） |

---

## 四、注意事项与缺口

1. **app_id**：在 pay_channel、pay_order、refund_order 中出现，但当前**没有商户应用主表**（如 merchant.mch_app）。若要做多应用接入，建议新增 mch_app，并保证 app_id 在该表中存在。
2. **ref_type / ref_no**：merchant_fund_flow、agent_fund_flow 用 ref_type + ref_no 关联业务单；ref_no 长度 64，可容纳 pay_order_no、refund_order_no、settlement_no 等，**长度一致**。
3. **settlement_record.acc_no**：同时表示商户号或代理号，依赖 acc_type 区分；与 merchant.mch_no、agent.agent_no 均为 VARCHAR(32)，**一致**。
4. **未建外键**：所有关系均为逻辑关联，应用层需保证：先有商户再建 pay_channel/下单、先有 pay_order 再建 refund_order 与 callback_log、先有 reconcile_batch 再写 reconcile_detail 等。

以上表关系与一致性已按当前 001～004 的 SQL 检查完毕。
