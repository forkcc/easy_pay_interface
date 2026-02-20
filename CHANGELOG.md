# Changelog

## [1.1.0] - 2026-02-21

### Added
- **easy-pay-task** module: independent scheduled task runner (Dubbo consumer)
  - `OrderExpireJob` — close expired unpaid orders (every 5 min)
  - `NotifyRetryJob` — retry merchant HTTP callbacks (every 1 min)
  - `DailyStatJob` — aggregate daily order statistics (00:05 daily)
  - `AutoSettleJob` — process pending settlement records (02:30 daily)
- Task configuration via `TaskProperties` with per-job cron & batch size
- Thread pool scheduler (4 threads) with graceful shutdown
- `TASK_ENABLED` environment variable for global task switch

### Changed
- Flyway migrations split from 2 files to 10 domain-specific files (V1~V10)
- All Flyway SQL columns now include COMMENT annotations
- README updated with task module docs, Flyway migration table, project tree

## [1.0.0] - 2026-02-20

### Added
- Initial project structure with easy-pay-api and easy-pay-provider modules
- 6 business domains: Merchant, Payment, Agent, Account/Settlement, Statistics, System Management
- 15 Dubbo service interfaces with full CRUD implementations
- 25 database tables with MySQL partitioning for pay orders
- Daily aggregation statistics table (t_order_stat_daily)
- Flyway database migration (V1~V10, split by domain)
- Dubbo Token authentication for service access control
- Spring Data JPA with HikariCP connection pool optimization
- Apache License 2.0
