# 与 Jeepay / 龙果支付 对比：缺失表分析

参考 [Jeepay](https://github.com/jeequan/jeepay)（计全支付）、[龙果支付 roncoo-pay](https://github.com/roncoo/roncoo-pay)，从**商户、代理、支付、管理**四类维度整理当前已有表与建议补充表。

---

## 一、当前已有表（按 schema）

| Schema   | 表 | 说明 |
|----------|----|------|
| merchant | merchant | 商户主表（含登录、OTP、余额/冻结余额） |
| merchant | merchant_fund_flow | 商户资金流水 |
| merchant | merchant_callback_log | 商户异步通知回调记录 |
| agent    | agent | 代理主表（含登录、OTP、余额/冻结余额） |
| agent    | agent_fund_flow | 代理资金流水 |
| agent    | agent_merchant | 代理与商户绑定关系 |
| payment  | pay_type | 支付类型 |
| payment  | pay_interface | 支付接口 |
| payment  | pay_channel | 支付渠道（商户+应用+方式） |
| payment  | pay_order | 支付订单 |
| payment  | order_record | 订单状态变更记录 |
| payment  | refund_order | 退款订单 |
| payment  | reconcile_batch | 对账批次 |
| payment  | reconcile_detail | 对账明细/差错 |
| payment  | settlement_record | 结算记录 |
| manager  | manager_user | 管理端登录账号 |

---

## 二、已有表详细用途说明

### merchant 商户模块

| 表 | 用途说明 |
|----|----------|
| **merchant.merchant** | 商户主数据与登录：商户号(mch_no)、名称、联系人、状态；登录名、密码哈希、Google OTP；**可用余额(balance)、冻结余额(frozen_balance)**（单位分）。用于商户注册/登录、余额展示与扣款前校验。 |
| **merchant.merchant_fund_flow** | 商户资金流水（只追加、不修改）：每笔入账/出账/冻结/解冻记录金额、变动前后余额、关联业务类型(ref_type)与单号(ref_no)。用于对账、历史查询、审计；与主表 balance 配合，流水为明细、主表为当前快照。 |
| **merchant.merchant_callback_log** | 支付成功后的**商户异步通知**记录：按 pay_order_no 记录回调 URL、已回调次数、最近一次 HTTP 状态码与响应、回调状态、下次计划回调时间。用于重试调度、排查通知失败。 |

### agent 代理模块

| 表 | 用途说明 |
|----|----------|
| **agent.agent** | 代理主数据与登录：代理号(agent_no)、名称、联系人、状态；登录名、密码哈希、OTP；**可用余额、冻结余额**。用于代理登录、分成入账与余额展示。 |
| **agent.agent_fund_flow** | 代理资金流水：与商户流水结构一致，记录代理侧入账/出账/冻结/解冻及关联业务，用于代理对账与报表。 |
| **agent.agent_merchant** | 代理与商户的**绑定关系**：一个代理可绑定多个商户(agent_no + mch_no 唯一)，含绑定时间、状态(绑定/解绑)。用于控制“哪些商户归属该代理”、分成与数据隔离。 |

### payment 支付模块

| 表 | 用途说明 |
|----|----------|
| **payment.pay_type** | **支付类型**字典：如条码支付、扫码支付、H5 等；type_code、type_name、排序、状态。用于前端展示支付方式分类、与 pay_interface 关联。 |
| **payment.pay_interface** | **支付接口**字典：如微信、支付宝等；if_code、if_name、归属 type_code、状态。与 pay_channel 的 if_code 对应，表示接入了哪家渠道。 |
| **payment.pay_channel** | **支付渠道**配置：维度为「商户+应用(app_id)+支付方式(way_code)」。存渠道侧商户号、商户名、扩展 config(JSONB)。用于下单时根据 mch_no+app_id+way_code 选通道。 |
| **payment.pay_order** | **支付订单**：订单号、商户号、应用、接口/方式、金额、币种、状态(待支付/已支付/已关闭/已退款)、渠道订单号、过期/成功/关闭时间、通知与跳转 URL。核心业务表，支付与回调均围绕此表。 |
| **payment.order_record** | **订单状态变更记录**：按 pay_order_no 记录事件(create/pay/close/refund/notify)、旧状态、新状态、扩展 JSON。用于排查问题、审计与对账辅助。 |
| **payment.refund_order** | **退款订单**：退款单号、原支付订单号、商户号、应用、退款金额、状态(申请中/成功/失败/已关闭)、渠道退款单号、申请时间、成功时间。独立退款单便于对账与查询。 |
| **payment.reconcile_batch** | **对账批次**：按渠道(channel)+账单日(bill_date)创建批次，记录总笔数、一致笔数、差错笔数、状态(处理中/已完成/异常)。用于 T+1 渠道对账任务管理。 |
| **payment.reconcile_detail** | **对账明细/差错**：每笔平台订单与渠道账单比对结果；记录平台订单号、渠道订单号、金额、双方状态、差异类型(一致/平台多/渠道多/金额或状态不一致)、差异说明。用于差错处理与报表。 |
| **payment.settlement_record** | **结算记录**：结算单号、账户类型(商户/代理)、账户号、结算周期、结算金额、手续费、状态(待打款/已打款/失败)、打款时间、打款流水号。用于结算与打款管理。 |

### manager 管理端模块

| 表 | 用途说明 |
|----|----------|
| **manager.manager_user** | **管理后台登录账号**：登录名、密码哈希、Google OTP、真实姓名、状态、最后登录时间。仅用于运营/管理员登录后台，与商户/代理登录隔离。 |

---

## 三、建议补充表（与两工程对比后仍缺失部分）

**说明**：对账表 reconcile_batch、reconcile_detail 已在 003_payment.sql 中；商户/代理余额已在 merchant.merchant、agent.agent 主表，若不做独立账户子表可不再加 merchant_account/agent_account。

### 1. 商户相关

| 建议表 | 说明 | 参考 |
|--------|------|------|
| **merchant.mch_app** | 商户应用表。一个商户多个应用，每应用有 app_id、应用名称、回调域名/地址、状态等。pay_order / pay_channel 中的 app_id 需从此表取主数据。 | Jeepay 多应用接入 |
| **merchant.merchant_account** | （可选）商户独立账户表。当前余额已在 merchant 主表，若需与流水完全分离或多币种/多账户可再加。 | 龙果 rp_account |

### 2. 代理相关

| 建议表 | 说明 | 参考 |
|--------|------|------|
| **agent.agent_account** | （可选）代理独立账户表。同上，当前余额已在 agent 主表。 | 同上 |

### 3. 支付相关

| 建议表 | 说明 | 参考 |
|--------|------|------|
| **merchant.withdraw_order** 或 **payment.withdraw_order** | 提现订单表。商户/代理提现申请、审核状态、打款流水号、手续费等。 | 常见支付系统均有 |

### 4. 管理相关

| 建议表 | 说明 | 参考 |
|--------|------|------|
| **manager.manager_role** | 角色表。角色编码、名称、备注。 | Jeepay Spring Security、龙果权限 |
| **manager.manager_user_role** | 用户角色关联表。manager_user_id、role_id。 | 多对多 |
| **manager.manager_permission** | 权限/菜单表。权限码、名称、类型（菜单/按钮）、父级、路径等。 | 后台权限体系 |
| **manager.manager_role_permission** | 角色权限关联表。 | 角色授权 |
| **manager.manager_oper_log** | 管理端操作日志/审计日志。操作人、模块、操作类型、请求参数、IP、时间。 | 安全与审计 |

### 5. 其他（可选）

| 建议表 | 说明 | 参考 |
|--------|------|------|
| **payment.pay_way** | 支付方式表。way_code、way_name、归属 if_code、排序等。当前 way_code 在 pay_channel / pay_order 中直接使用，若需集中维护方式列表可加。 | Jeepay 支付产品/方式配置 |
| **公共或各 schema 的 sys_config** | 系统参数/配置表。键值对或分组配置，用于开关、限额等。 | 常见 |

---

## 四、优先级建议

| 优先级 | 表 | 理由 |
|--------|----|------|
| 高 | merchant.mch_app | 多应用接入必备，pay_order/pay_channel 依赖 app 主数据 |
| 中 | withdraw_order | 若支持商户/代理提现则必须 |
| 低 | manager 角色权限四张表 | 管理端做细粒度权限、菜单时需要 |
| 低 | manager.manager_oper_log | 安全与审计需要时加 |

---

## 五、小结

- **已有**：商户/代理主体（含余额）、资金流水、回调日志；支付类型/接口/渠道/订单/订单记录、**退款订单**、对账批次与对账明细、**结算记录**；管理端登录账号。已覆盖基础支付、退款、对账、结算与登录。
- **仍缺且建议补**：**商户应用(mch_app)** 为多应用必备；**提现订单(withdraw_order)** 及**管理端角色权限与操作日志**按业务阶段再补。

如需，可在 `migrations/` 下新增 005、006… 的 SQL 文件实现上述部分或全部表结构（含完整备注、避开关键词）。
