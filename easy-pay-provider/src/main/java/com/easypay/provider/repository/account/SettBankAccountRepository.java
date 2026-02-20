package com.easypay.provider.repository.account;

import com.easypay.provider.entity.account.SettBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SettBankAccountRepository extends JpaRepository<SettBankAccount, Long> {

    List<SettBankAccount> findByAccountNo(String accountNo);
}
