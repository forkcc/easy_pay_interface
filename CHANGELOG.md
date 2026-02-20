# Changelog

## [1.0.0] - 2026-02-20

### Added
- Initial project structure with easy-pay-api and easy-pay-provider modules
- 6 business domains: Merchant, Payment, Agent, Account/Settlement, Statistics, System Management
- 15 Dubbo service interfaces with full CRUD implementations
- 25 database tables with MySQL partitioning for pay orders
- Daily aggregation statistics table (t_order_stat_daily)
- Flyway database migration (V1 schema + V2 seed data)
- Dubbo Token authentication for service access control
- Spring Data JPA with HikariCP connection pool optimization
- Apache License 2.0
