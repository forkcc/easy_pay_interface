package com.easypay.api.service.account;

import com.easypay.api.dto.account.AccountBalanceDTO;
import com.easypay.api.dto.account.AccountHistoryDTO;
import com.easypay.api.result.PageResult;

public interface IAccountService {

    AccountBalanceDTO getByAccountNo(String accountNo);

    boolean initAccount(String accountNo, String accountName, Byte accountType);

    boolean changeBalance(String accountNo, Byte changeType, Long changeAmount, String bizOrderNo, String bizType, String remark);

    boolean freeze(String accountNo, Long amount, String bizOrderNo);

    boolean unfreeze(String accountNo, Long amount, String bizOrderNo);

    PageResult<AccountHistoryDTO> pageHistory(String accountNo, int pageNum, int pageSize);
}
