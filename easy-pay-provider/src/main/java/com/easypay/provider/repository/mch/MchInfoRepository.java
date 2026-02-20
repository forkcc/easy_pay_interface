package com.easypay.provider.repository.mch;

import com.easypay.provider.entity.mch.MchInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MchInfoRepository extends JpaRepository<MchInfo, String> {

    Page<MchInfo> findByAgentNo(String agentNo, Pageable pageable);
}
