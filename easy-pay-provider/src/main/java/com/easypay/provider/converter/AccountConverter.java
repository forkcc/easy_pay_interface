package com.easypay.provider.converter;

import com.easypay.api.dto.account.AccountBalanceDTO;
import com.easypay.api.dto.account.AccountHistoryDTO;
import com.easypay.api.dto.account.SettBankAccountDTO;
import com.easypay.api.dto.account.SettRecordDTO;
import com.easypay.provider.entity.account.AccountBalance;
import com.easypay.provider.entity.account.AccountHistory;
import com.easypay.provider.entity.account.SettBankAccount;
import com.easypay.provider.entity.account.SettRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 账户域对象转换器，负责账户余额、账户流水、结算记录、结算银行账户等实体与对应 DTO 之间的相互转换
 */
@Component
public class AccountConverter {

    public static AccountBalanceDTO toDTO(AccountBalance entity) {
        if (entity == null) {
            return null;
        }
        AccountBalanceDTO dto = new AccountBalanceDTO();
        dto.setId(entity.getId());
        dto.setAccountNo(entity.getAccountNo());
        dto.setAccountName(entity.getAccountName());
        dto.setAccountType(entity.getAccountType());
        dto.setBalance(entity.getBalance());
        dto.setFrozenAmount(entity.getFrozenAmount());
        dto.setUnsettledAmount(entity.getUnsettledAmount());
        dto.setSettledAmount(entity.getSettledAmount());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static AccountBalance toEntity(AccountBalanceDTO dto) {
        if (dto == null) {
            return null;
        }
        AccountBalance entity = new AccountBalance();
        entity.setId(dto.getId());
        entity.setAccountNo(dto.getAccountNo());
        entity.setAccountName(dto.getAccountName());
        entity.setAccountType(dto.getAccountType());
        entity.setBalance(dto.getBalance());
        entity.setFrozenAmount(dto.getFrozenAmount());
        entity.setUnsettledAmount(dto.getUnsettledAmount());
        entity.setSettledAmount(dto.getSettledAmount());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<AccountBalanceDTO> toAccountBalanceDTOList(List<AccountBalance> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<AccountBalanceDTO> result = new ArrayList<>(entities.size());
        for (AccountBalance entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static AccountHistoryDTO toDTO(AccountHistory entity) {
        if (entity == null) {
            return null;
        }
        AccountHistoryDTO dto = new AccountHistoryDTO();
        dto.setId(entity.getId());
        dto.setAccountNo(entity.getAccountNo());
        dto.setChangeType(entity.getChangeType());
        dto.setChangeAmount(entity.getChangeAmount());
        dto.setBalanceBefore(entity.getBalanceBefore());
        dto.setBalanceAfter(entity.getBalanceAfter());
        dto.setBizOrderNo(entity.getBizOrderNo());
        dto.setBizType(entity.getBizType());
        dto.setRemark(entity.getRemark());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static AccountHistory toEntity(AccountHistoryDTO dto) {
        if (dto == null) {
            return null;
        }
        AccountHistory entity = new AccountHistory();
        entity.setId(dto.getId());
        entity.setAccountNo(dto.getAccountNo());
        entity.setChangeType(dto.getChangeType());
        entity.setChangeAmount(dto.getChangeAmount());
        entity.setBalanceBefore(dto.getBalanceBefore());
        entity.setBalanceAfter(dto.getBalanceAfter());
        entity.setBizOrderNo(dto.getBizOrderNo());
        entity.setBizType(dto.getBizType());
        entity.setRemark(dto.getRemark());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }

    public static List<AccountHistoryDTO> toAccountHistoryDTOList(List<AccountHistory> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<AccountHistoryDTO> result = new ArrayList<>(entities.size());
        for (AccountHistory entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static SettRecordDTO toDTO(SettRecord entity) {
        if (entity == null) {
            return null;
        }
        SettRecordDTO dto = new SettRecordDTO();
        dto.setId(entity.getId());
        dto.setSettNo(entity.getSettNo());
        dto.setAccountNo(entity.getAccountNo());
        dto.setMchNo(entity.getMchNo());
        dto.setAgentNo(entity.getAgentNo());
        dto.setSettAmount(entity.getSettAmount());
        dto.setFeeAmount(entity.getFeeAmount());
        dto.setRemitAmount(entity.getRemitAmount());
        dto.setState(entity.getState());
        dto.setBankAccountNo(entity.getBankAccountNo());
        dto.setBankAccountName(entity.getBankAccountName());
        dto.setBankName(entity.getBankName());
        dto.setErrMsg(entity.getErrMsg());
        dto.setSuccessTime(entity.getSuccessTime());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static SettRecord toEntity(SettRecordDTO dto) {
        if (dto == null) {
            return null;
        }
        SettRecord entity = new SettRecord();
        entity.setId(dto.getId());
        entity.setSettNo(dto.getSettNo());
        entity.setAccountNo(dto.getAccountNo());
        entity.setMchNo(dto.getMchNo());
        entity.setAgentNo(dto.getAgentNo());
        entity.setSettAmount(dto.getSettAmount());
        entity.setFeeAmount(dto.getFeeAmount());
        entity.setRemitAmount(dto.getRemitAmount());
        entity.setState(dto.getState());
        entity.setBankAccountNo(dto.getBankAccountNo());
        entity.setBankAccountName(dto.getBankAccountName());
        entity.setBankName(dto.getBankName());
        entity.setErrMsg(dto.getErrMsg());
        entity.setSuccessTime(dto.getSuccessTime());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<SettRecordDTO> toSettRecordDTOList(List<SettRecord> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<SettRecordDTO> result = new ArrayList<>(entities.size());
        for (SettRecord entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static SettBankAccountDTO toDTO(SettBankAccount entity) {
        if (entity == null) {
            return null;
        }
        SettBankAccountDTO dto = new SettBankAccountDTO();
        dto.setId(entity.getId());
        dto.setAccountNo(entity.getAccountNo());
        dto.setMchNo(entity.getMchNo());
        dto.setBankName(entity.getBankName());
        dto.setBankBranch(entity.getBankBranch());
        dto.setBankAccountNo(entity.getBankAccountNo());
        dto.setBankAccountName(entity.getBankAccountName());
        dto.setAccountType(entity.getAccountType());
        dto.setState(entity.getState());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static SettBankAccount toEntity(SettBankAccountDTO dto) {
        if (dto == null) {
            return null;
        }
        SettBankAccount entity = new SettBankAccount();
        entity.setId(dto.getId());
        entity.setAccountNo(dto.getAccountNo());
        entity.setMchNo(dto.getMchNo());
        entity.setBankName(dto.getBankName());
        entity.setBankBranch(dto.getBankBranch());
        entity.setBankAccountNo(dto.getBankAccountNo());
        entity.setBankAccountName(dto.getBankAccountName());
        entity.setAccountType(dto.getAccountType());
        entity.setState(dto.getState());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<SettBankAccountDTO> toSettBankAccountDTOList(List<SettBankAccount> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<SettBankAccountDTO> result = new ArrayList<>(entities.size());
        for (SettBankAccount entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }
}
