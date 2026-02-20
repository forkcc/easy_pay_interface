package com.easypay.provider.repository.account;

import com.easypay.provider.entity.account.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 账户余额数据访问接口，对应实体 {@link AccountBalance}。
 */
public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {

    Optional<AccountBalance> findByAccountNo(String accountNo);
}
