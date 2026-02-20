package com.easypay.provider.repository.mch;

import com.easypay.provider.entity.mch.MchApp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 商户应用数据访问接口，对应实体 {@link MchApp}。
 */
public interface MchAppRepository extends JpaRepository<MchApp, String> {

    List<MchApp> findByMchNo(String mchNo);

    Page<MchApp> findByMchNo(String mchNo, Pageable pageable);
}
