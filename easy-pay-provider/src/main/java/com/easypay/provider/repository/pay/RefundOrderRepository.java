package com.easypay.provider.repository.pay;

import com.easypay.provider.entity.pay.RefundOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefundOrderRepository extends JpaRepository<RefundOrder, String> {

    Optional<RefundOrder> findByMchNoAndMchRefundNo(String mchNo, String mchRefundNo);

    List<RefundOrder> findByPayOrderId(String payOrderId);
}
