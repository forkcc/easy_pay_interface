package com.easypay.provider.repository.pay;

import com.easypay.provider.entity.pay.PayInterfaceConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 支付接口配置数据访问接口，对应实体 {@link PayInterfaceConfig}。
 */
public interface PayInterfaceConfigRepository extends JpaRepository<PayInterfaceConfig, Long> {

    Optional<PayInterfaceConfig> findByInfoTypeAndInfoIdAndIfCode(String infoType, String infoId, String ifCode);

    List<PayInterfaceConfig> findByInfoTypeAndInfoId(String infoType, String infoId);
}
