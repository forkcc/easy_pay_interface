package com.easypay.provider.repository.pay;

import com.easypay.provider.entity.pay.TransferOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 转账订单数据访问接口，对应实体 {@link TransferOrder}。
 */
public interface TransferOrderRepository extends JpaRepository<TransferOrder, String> {

    Optional<TransferOrder> findByMchNoAndMchTransferNo(String mchNo, String mchTransferNo);
}
