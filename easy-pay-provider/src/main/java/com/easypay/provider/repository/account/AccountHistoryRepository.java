package com.easypay.provider.repository.account;

import com.easypay.provider.entity.account.AccountHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long> {

    Page<AccountHistory> findByAccountNoOrderByCreatedAtDesc(String accountNo, Pageable pageable);
}
