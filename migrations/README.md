# 按模块区分 Schema 的建表脚本（PostgreSQL）

每个模块使用独立 **schema**，表名为 `schema.表名`。

| 文件 | Schema | 表 |
|------|--------|-----|
| 001_merchant.sql | merchant | merchant.merchant, merchant.merchant_fund_flow, merchant.merchant_callback_log |
| 002_agent.sql    | agent    | agent.agent, agent.agent_fund_flow, agent.agent_merchant |
| 003_payment.sql  | payment  | pay_type, pay_interface, pay_channel, pay_order, order_record, refund_order, reconcile_batch, reconcile_detail, settlement_record |
| 004_manager.sql  | manager  | manager.manager_user（管理端账号密码 + Google OTP）；商户/代理登录在 merchant.merchant、agent.agent 内 |
| 005_manager_default_user.sql | manager | 插入默认管理员：登录名 admin，默认密码 password，首次登录后请修改 |

---

## 如何导入

### 1. 创建数据库（若尚未创建）

```bash
psql -U postgres -c "CREATE DATABASE easy_pay ENCODING 'UTF8';"
```

### 2. 按顺序执行建表脚本

在项目根目录下执行（或把路径换成你的实际路径）：

```bash
export PGHOST=localhost PGPORT=5432 PGUSER=postgres PGPASSWORD=postgres PGDATABASE=easy_pay

psql -f migrations/001_merchant.sql
psql -f migrations/002_agent.sql
psql -f migrations/003_payment.sql
psql -f migrations/004_manager.sql
psql -f migrations/005_manager_default_user.sql
```

或单行指定连接信息：

```bash
psql "postgresql://postgres:postgres@localhost:5432/easy_pay?sslmode=disable" -f migrations/001_merchant.sql
psql "postgresql://postgres:postgres@localhost:5432/easy_pay?sslmode=disable" -f migrations/002_agent.sql
psql "postgresql://postgres:postgres@localhost:5432/easy_pay?sslmode=disable" -f migrations/003_payment.sql
psql "postgresql://postgres:postgres@localhost:5432/easy_pay?sslmode=disable" -f migrations/004_manager.sql
psql "postgresql://postgres:postgres@localhost:5432/easy_pay?sslmode=disable" -f migrations/005_manager_default_user.sql
```

### 3. 验证 schema 与表

```bash
psql -U postgres -d easy_pay -c "\dn"
psql -U postgres -d easy_pay -c "\dt merchant.*"
psql -U postgres -d easy_pay -c "\dt agent.*"
psql -U postgres -d easy_pay -c "\dt payment.*"
psql -U postgres -d easy_pay -c "\dt manager.*"
psql -U postgres -d easy_pay -c "\dt payment.*"
```

---

## 应用连接与 search_path

- **连接串**：仍连同一个库 `easy_pay`，例如  
  `host=localhost user=postgres password=postgres dbname=easy_pay port=5432 sslmode=disable`

- **访问表**有两种方式：
  1. **带 schema 全名**：`SELECT * FROM merchant.merchant;`、`SELECT * FROM payment.pay_order;`
  2. **设置 search_path**：  
     `SET search_path TO merchant;` 后可直接写 `SELECT * FROM merchant;`  
     或在连接串里加：  
     `options=-c%20search_path%3Dmerchant`（URL 编码后为 `options=-c search_path=merchant`）

- **GORM**：在连接时设置默认 schema，例如：
  ```go
  db.Exec("SET search_path TO merchant")
  ```
  或每个模型指定表名：`Table("merchant.merchant")`。
