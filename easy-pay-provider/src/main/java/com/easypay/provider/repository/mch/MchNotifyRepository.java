package com.easypay.provider.repository.mch;

import com.easypay.provider.entity.mch.MchNotify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MchNotifyRepository extends JpaRepository<MchNotify, Long> {

    List<MchNotify> findByStateOrderByCreatedAtAsc(Byte state, Pageable pageable);

    Page<MchNotify> findByMchNo(String mchNo, Pageable pageable);

    Page<MchNotify> findByMchNoAndOrderId(String mchNo, String orderId, Pageable pageable);
}
