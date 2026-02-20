package com.easypay.provider.repository.sys;

import com.easypay.provider.entity.sys.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 系统配置数据访问接口，对应实体 {@link SysConfig}。
 */
public interface SysConfigRepository extends JpaRepository<SysConfig, String> {

    List<SysConfig> findByConfigGroup(String configGroup);
}
