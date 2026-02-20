package com.easypay.provider.service.impl.account;

import com.easypay.api.dto.account.SettBankAccountDTO;
import com.easypay.api.dto.account.SettRecordDTO;
import com.easypay.api.enums.SettStatusEnum;
import com.easypay.api.result.PageResult;
import com.easypay.api.service.account.ISettService;
import com.easypay.provider.converter.AccountConverter;
import com.easypay.provider.entity.account.SettBankAccount;
import com.easypay.provider.entity.account.SettRecord;
import com.easypay.provider.repository.account.SettBankAccountRepository;
import com.easypay.provider.repository.account.SettRecordRepository;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@DubboService
public class SettServiceImpl implements ISettService {

    @Resource
    private SettRecordRepository settRecordRepository;

    @Resource
    private SettBankAccountRepository settBankAccountRepository;

    @Override
    @Transactional
    public SettRecordDTO createSettRecord(SettRecordDTO dto) {
        SettRecord entity = AccountConverter.toEntity(dto);
        entity = settRecordRepository.save(entity);
        return AccountConverter.toDTO(entity);
    }

    @Override
    public SettRecordDTO getSettRecordById(Long id) {
        return settRecordRepository.findById(id).map(AccountConverter::toDTO).orElse(null);
    }

    @Override
    public SettRecordDTO getSettRecordBySettNo(String settNo) {
        return settRecordRepository.findBySettNo(settNo).map(AccountConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean updateSettState(Long id, Byte state, String errMsg) {
        return settRecordRepository.findById(id).map(entity -> {
            entity.setState(state);
            entity.setErrMsg(errMsg);
            if (SettStatusEnum.SUCCESS.getCode() == state) {
                entity.setSuccessTime(LocalDateTime.now());
            }
            settRecordRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    public PageResult<SettRecordDTO> pageSettRecord(String mchNo, Byte state, int pageNum, int pageSize) {
        Page<SettRecord> page;
        if (state != null) {
            page = settRecordRepository.findByMchNoAndState(mchNo, state, PageRequest.of(pageNum - 1, pageSize));
        } else {
            page = settRecordRepository.findByMchNo(mchNo, PageRequest.of(pageNum - 1, pageSize));
        }
        List<SettRecordDTO> dtoList = AccountConverter.toSettRecordDTOList(page.getContent());
        return new PageResult<>(dtoList, page.getTotalElements(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public SettBankAccountDTO createBankAccount(SettBankAccountDTO dto) {
        SettBankAccount entity = AccountConverter.toEntity(dto);
        entity = settBankAccountRepository.save(entity);
        return AccountConverter.toDTO(entity);
    }

    @Override
    public SettBankAccountDTO getBankAccountById(Long id) {
        return settBankAccountRepository.findById(id).map(AccountConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean updateBankAccount(SettBankAccountDTO dto) {
        return settBankAccountRepository.findById(dto.getId()).map(entity -> {
            entity.setBankName(dto.getBankName());
            entity.setBankBranch(dto.getBankBranch());
            entity.setBankAccountNo(dto.getBankAccountNo());
            entity.setBankAccountName(dto.getBankAccountName());
            entity.setAccountType(dto.getAccountType());
            entity.setState(dto.getState());
            settBankAccountRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    @Transactional
    public boolean deleteBankAccount(Long id) {
        settBankAccountRepository.deleteById(id);
        return true;
    }

    @Override
    public List<SettBankAccountDTO> listBankAccount(String accountNo) {
        return AccountConverter.toSettBankAccountDTOList(settBankAccountRepository.findByAccountNo(accountNo));
    }
}
