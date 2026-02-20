package com.easypay.provider.repository.pay;

import com.easypay.provider.entity.pay.PayInterfaceConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayInterfaceConfigRepository extends JpaRepository<PayInterfaceConfig, Long> {

    Optional<PayInterfaceConfig> findByInfoTypeAndInfoIdAndIfCode(String infoType, String infoId, String ifCode);

    List<PayInterfaceConfig> findByInfoTypeAndInfoId(String infoType, String infoId);
}
