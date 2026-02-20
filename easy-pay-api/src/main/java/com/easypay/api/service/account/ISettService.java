package com.easypay.api.service.account;

import com.easypay.api.dto.account.SettBankAccountDTO;
import com.easypay.api.dto.account.SettRecordDTO;
import com.easypay.api.result.PageResult;
import java.util.List;

/**
 * 结算服务接口
 */
public interface ISettService {

    // 创建结算记录
    SettRecordDTO createSettRecord(SettRecordDTO dto);

    // 根据ID查询结算记录
    SettRecordDTO getSettRecordById(Long id);

    // 根据结算单号查询结算记录
    SettRecordDTO getSettRecordBySettNo(String settNo);

    // 更新结算状态
    boolean updateSettState(Long id, Byte state, String errMsg);

    // 分页查询结算记录
    PageResult<SettRecordDTO> pageSettRecord(String mchNo, Byte state, int pageNum, int pageSize);

    // 创建结算银行账户
    SettBankAccountDTO createBankAccount(SettBankAccountDTO dto);

    // 根据ID查询结算银行账户
    SettBankAccountDTO getBankAccountById(Long id);

    // 更新结算银行账户
    boolean updateBankAccount(SettBankAccountDTO dto);

    // 删除结算银行账户
    boolean deleteBankAccount(Long id);

    // 查询指定账户下的银行账户列表
    List<SettBankAccountDTO> listBankAccount(String accountNo);
}
