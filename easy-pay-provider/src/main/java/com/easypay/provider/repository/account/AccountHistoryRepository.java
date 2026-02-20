package com.easypay.provider.repository.account;

import com.easypay.provider.entity.account.AccountHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 账户变动历史数据访问接口，对应实体 {@link AccountHistory}。
 */
public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long> {

    Page<AccountHistory> findByAccountNoOrderByCreatedAtDesc(String accountNo, Pageable pageable);
}
