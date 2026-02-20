package com.easypay.provider.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA 持久层配置，启用 JPA 仓储扫描和事务管理
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.easypay.provider.repository")
@EnableTransactionManagement
public class JpaConfig {
}
