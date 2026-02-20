package com.easypay.provider.repository.mch;

import com.easypay.provider.entity.mch.MchApp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MchAppRepository extends JpaRepository<MchApp, String> {

    List<MchApp> findByMchNo(String mchNo);

    Page<MchApp> findByMchNo(String mchNo, Pageable pageable);
}
