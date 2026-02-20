package com.easypay.provider.repository.pay;

import com.easypay.provider.entity.pay.PayPassage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 支付通道数据访问接口，对应实体 {@link PayPassage}。
 */
public interface PayPassageRepository extends JpaRepository<PayPassage, Long> {

    List<PayPassage> findByState(Byte state);
}
