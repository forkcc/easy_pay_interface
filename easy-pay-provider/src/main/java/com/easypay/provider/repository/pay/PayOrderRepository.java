package com.easypay.provider.repository.pay;

import com.easypay.provider.entity.pay.PayOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PayOrderRepository extends JpaRepository<PayOrder, String> {

    Optional<PayOrder> findByMchNoAndMchOrderNo(String mchNo, String mchOrderNo);

    @Query("SELECT SUM(p.amount) FROM PayOrder p WHERE p.mchNo = :mchNo AND p.state = 2 AND p.createdAt BETWEEN :start AND :end")
    Long sumSuccessAmount(@Param("mchNo") String mchNo, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
