package com.easypay.provider.repository.account;

import com.easypay.provider.entity.account.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {

    Optional<AccountBalance> findByAccountNo(String accountNo);
}
