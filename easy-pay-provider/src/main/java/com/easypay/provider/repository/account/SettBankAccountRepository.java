package com.easypay.provider.repository.account;

import com.easypay.provider.entity.account.SettBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 结算银行账户数据访问接口，对应实体 {@link SettBankAccount}。
 */
public interface SettBankAccountRepository extends JpaRepository<SettBankAccount, Long> {

    List<SettBankAccount> findByAccountNo(String accountNo);
}
