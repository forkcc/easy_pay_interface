package com.easypay.api.service.account;

import com.easypay.api.dto.account.SettBankAccountDTO;
import com.easypay.api.dto.account.SettRecordDTO;
import com.easypay.api.result.PageResult;
import java.util.List;

public interface ISettService {

    SettRecordDTO createSettRecord(SettRecordDTO dto);

    SettRecordDTO getSettRecordById(Long id);

    SettRecordDTO getSettRecordBySettNo(String settNo);

    boolean updateSettState(Long id, Byte state, String errMsg);

    PageResult<SettRecordDTO> pageSettRecord(String mchNo, Byte state, int pageNum, int pageSize);

    SettBankAccountDTO createBankAccount(SettBankAccountDTO dto);

    SettBankAccountDTO getBankAccountById(Long id);

    boolean updateBankAccount(SettBankAccountDTO dto);

    boolean deleteBankAccount(Long id);

    List<SettBankAccountDTO> listBankAccount(String accountNo);
}
