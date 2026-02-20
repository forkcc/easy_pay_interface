package com.easypay.api.service.account;

import com.easypay.api.dto.account.AccountBalanceDTO;
import com.easypay.api.dto.account.AccountHistoryDTO;
import com.easypay.api.result.PageResult;

/**
 * 账户服务接口
 */
public interface IAccountService {

    // 根据账户编号查询账户余额信息
    AccountBalanceDTO getByAccountNo(String accountNo);

    // 初始化账户
    boolean initAccount(String accountNo, String accountName, Byte accountType);

    // 变更账户余额
    boolean changeBalance(String accountNo, Byte changeType, Long changeAmount, String bizOrderNo, String bizType, String remark);

    // 冻结账户资金
    boolean freeze(String accountNo, Long amount, String bizOrderNo);

    // 解冻账户资金
    boolean unfreeze(String accountNo, Long amount, String bizOrderNo);

    // 分页查询账户变动历史
    PageResult<AccountHistoryDTO> pageHistory(String accountNo, int pageNum, int pageSize);
}
