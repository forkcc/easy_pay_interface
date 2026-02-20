package com.easypay.provider.service.impl.account;

import com.easypay.api.dto.account.AccountBalanceDTO;
import com.easypay.api.dto.account.AccountHistoryDTO;
import com.easypay.api.enums.AccountChangeTypeEnum;
import com.easypay.api.result.PageResult;
import com.easypay.api.service.account.IAccountService;
import com.easypay.provider.converter.AccountConverter;
import com.easypay.provider.entity.account.AccountBalance;
import com.easypay.provider.entity.account.AccountHistory;
import com.easypay.provider.repository.account.AccountBalanceRepository;
import com.easypay.provider.repository.account.AccountHistoryRepository;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DubboService
public class AccountServiceImpl implements IAccountService {

    @Resource
    private AccountBalanceRepository accountBalanceRepository;

    @Resource
    private AccountHistoryRepository accountHistoryRepository;

    @Override
    public AccountBalanceDTO getByAccountNo(String accountNo) {
        return accountBalanceRepository.findByAccountNo(accountNo).map(AccountConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean initAccount(String accountNo, String accountName, Byte accountType) {
        AccountBalance entity = new AccountBalance();
        entity.setAccountNo(accountNo);
        entity.setAccountName(accountName);
        entity.setAccountType(accountType);
        entity.setBalance(0L);
        entity.setFrozenAmount(0L);
        entity.setUnsettledAmount(0L);
        entity.setSettledAmount(0L);
        accountBalanceRepository.save(entity);
        return true;
    }

    @Override
    @Transactional
    public boolean changeBalance(String accountNo, Byte changeType, Long changeAmount, String bizOrderNo, String bizType, String remark) {
        return accountBalanceRepository.findByAccountNo(accountNo).map(account -> {
            long balanceBefore = account.getBalance();
            long balanceAfter;
            AccountChangeTypeEnum typeEnum = AccountChangeTypeEnum.fromCode(changeType);
            if (typeEnum == AccountChangeTypeEnum.INCOME) {
                account.setBalance(account.getBalance() + changeAmount);
                balanceAfter = account.getBalance();
            } else if (typeEnum == AccountChangeTypeEnum.EXPENSE) {
                account.setBalance(account.getBalance() - changeAmount);
                balanceAfter = account.getBalance();
            } else if (typeEnum == AccountChangeTypeEnum.FREEZE) {
                account.setBalance(account.getBalance() - changeAmount);
                account.setFrozenAmount(account.getFrozenAmount() + changeAmount);
                balanceAfter = account.getBalance();
            } else if (typeEnum == AccountChangeTypeEnum.UNFREEZE) {
                account.setBalance(account.getBalance() + changeAmount);
                account.setFrozenAmount(account.getFrozenAmount() - changeAmount);
                balanceAfter = account.getBalance();
            } else if (typeEnum == AccountChangeTypeEnum.SETTLE) {
                account.setUnsettledAmount(account.getUnsettledAmount() - changeAmount);
                account.setSettledAmount(account.getSettledAmount() + changeAmount);
                balanceAfter = balanceBefore;
            } else {
                return false;
            }
            accountBalanceRepository.save(account);
            AccountHistory history = new AccountHistory();
            history.setAccountNo(accountNo);
            history.setChangeType(changeType);
            history.setChangeAmount(changeAmount);
            history.setBalanceBefore(balanceBefore);
            history.setBalanceAfter(balanceAfter);
            history.setBizOrderNo(bizOrderNo);
            history.setBizType(bizType);
            history.setRemark(remark);
            accountHistoryRepository.save(history);
            return true;
        }).orElse(false);
    }

    @Override
    @Transactional
    public boolean freeze(String accountNo, Long amount, String bizOrderNo) {
        return changeBalance(accountNo, AccountChangeTypeEnum.FREEZE.getCode(), amount, bizOrderNo, null, null);
    }

    @Override
    @Transactional
    public boolean unfreeze(String accountNo, Long amount, String bizOrderNo) {
        return changeBalance(accountNo, AccountChangeTypeEnum.UNFREEZE.getCode(), amount, bizOrderNo, null, null);
    }

    @Override
    public PageResult<AccountHistoryDTO> pageHistory(String accountNo, int pageNum, int pageSize) {
        Page<AccountHistory> page = accountHistoryRepository.findByAccountNoOrderByCreatedAtDesc(accountNo, PageRequest.of(pageNum - 1, pageSize));
        List<AccountHistoryDTO> dtoList = AccountConverter.toAccountHistoryDTOList(page.getContent());
        return new PageResult<>(dtoList, page.getTotalElements(), pageNum, pageSize);
    }
}
