package com.easypay.provider.repository.account;

import com.easypay.provider.entity.account.SettRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 结算记录数据访问接口，对应实体 {@link SettRecord}。
 */
public interface SettRecordRepository extends JpaRepository<SettRecord, Long> {

    Optional<SettRecord> findBySettNo(String settNo);

    Page<SettRecord> findByMchNo(String mchNo, Pageable pageable);

    Page<SettRecord> findByMchNoAndState(String mchNo, Byte state, Pageable pageable);
}
