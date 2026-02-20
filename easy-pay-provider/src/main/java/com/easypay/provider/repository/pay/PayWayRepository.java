package com.easypay.provider.repository.pay;

import com.easypay.provider.entity.pay.PayWay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 支付方式数据访问接口，对应实体 {@link PayWay}。
 */
public interface PayWayRepository extends JpaRepository<PayWay, String> {

    List<PayWay> findByState(Byte state);
}
