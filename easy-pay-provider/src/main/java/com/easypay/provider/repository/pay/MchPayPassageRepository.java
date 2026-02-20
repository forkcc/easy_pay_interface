package com.easypay.provider.repository.pay;

import com.easypay.provider.entity.pay.MchPayPassage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MchPayPassageRepository extends JpaRepository<MchPayPassage, Long> {

    List<MchPayPassage> findByMchNoAndAppId(String mchNo, String appId);

    Optional<MchPayPassage> findByMchNoAndAppIdAndWayCodeAndState(String mchNo, String appId, String wayCode, Byte state);

    void deleteByMchNoAndAppId(String mchNo, String appId);
}
