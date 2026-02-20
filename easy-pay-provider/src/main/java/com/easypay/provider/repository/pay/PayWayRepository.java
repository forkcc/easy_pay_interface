package com.easypay.provider.repository.pay;

import com.easypay.provider.entity.pay.PayWay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayWayRepository extends JpaRepository<PayWay, String> {

    List<PayWay> findByState(Byte state);
}
