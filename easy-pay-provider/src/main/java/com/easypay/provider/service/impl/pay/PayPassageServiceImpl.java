package com.easypay.provider.service.impl.pay;

import com.easypay.api.dto.pay.MchPayPassageDTO;
import com.easypay.api.dto.pay.PayInterfaceConfigDTO;
import com.easypay.api.dto.pay.PayInterfaceDefineDTO;
import com.easypay.api.dto.pay.PayPassageDTO;
import com.easypay.api.dto.pay.PayWayDTO;
import com.easypay.api.service.pay.IPayPassageService;
import com.easypay.provider.converter.PayConverter;
import com.easypay.provider.entity.pay.MchPayPassage;
import com.easypay.provider.entity.pay.PayInterfaceConfig;
import com.easypay.provider.entity.pay.PayInterfaceDefine;
import com.easypay.provider.entity.pay.PayPassage;
import com.easypay.provider.entity.pay.PayWay;
import com.easypay.provider.repository.pay.MchPayPassageRepository;
import com.easypay.provider.repository.pay.PayInterfaceConfigRepository;
import com.easypay.provider.repository.pay.PayInterfaceDefineRepository;
import com.easypay.provider.repository.pay.PayPassageRepository;
import com.easypay.provider.repository.pay.PayWayRepository;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@DubboService
public class PayPassageServiceImpl implements IPayPassageService {

    @Resource
    private PayPassageRepository payPassageRepository;
    @Resource
    private PayWayRepository payWayRepository;
    @Resource
    private PayInterfaceDefineRepository payInterfaceDefineRepository;
    @Resource
    private PayInterfaceConfigRepository payInterfaceConfigRepository;
    @Resource
    private MchPayPassageRepository mchPayPassageRepository;

    private static final Byte STATE_ACTIVE = 1;

    @Override
    @Transactional
    public PayPassageDTO createPassage(PayPassageDTO dto) {
        PayPassage entity = PayConverter.toEntity(dto);
        entity = payPassageRepository.save(entity);
        return PayConverter.toDTO(entity);
    }

    @Override
    public PayPassageDTO getPassageById(Long passageId) {
        return payPassageRepository.findById(passageId).map(PayConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean updatePassage(PayPassageDTO dto) {
        return payPassageRepository.findById(dto.getPassageId()).map(entity -> {
            entity.setPassageName(dto.getPassageName());
            entity.setIfCode(dto.getIfCode());
            entity.setWayCode(dto.getWayCode());
            entity.setFeeRate(dto.getFeeRate());
            entity.setFeeAmount(dto.getFeeAmount());
            entity.setState(dto.getState());
            entity.setRemark(dto.getRemark());
            payPassageRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    public List<PayPassageDTO> listPassage(Byte state) {
        return PayConverter.toPayPassageDTOList(payPassageRepository.findByState(state));
    }

    @Override
    @Transactional
    public PayWayDTO createWay(PayWayDTO dto) {
        PayWay entity = PayConverter.toEntity(dto);
        entity = payWayRepository.save(entity);
        return PayConverter.toDTO(entity);
    }

    @Override
    public PayWayDTO getWayByCode(String wayCode) {
        return payWayRepository.findById(wayCode).map(PayConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean updateWay(PayWayDTO dto) {
        return payWayRepository.findById(dto.getWayCode()).map(entity -> {
            entity.setWayName(dto.getWayName());
            entity.setState(dto.getState());
            entity.setRemark(dto.getRemark());
            payWayRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    public List<PayWayDTO> listWay(Byte state) {
        return PayConverter.toPayWayDTOList(payWayRepository.findByState(state));
    }

    @Override
    @Transactional
    public PayInterfaceDefineDTO createIfDefine(PayInterfaceDefineDTO dto) {
        PayInterfaceDefine entity = PayConverter.toEntity(dto);
        entity = payInterfaceDefineRepository.save(entity);
        return PayConverter.toDTO(entity);
    }

    @Override
    public PayInterfaceDefineDTO getIfDefineByCode(String ifCode) {
        return payInterfaceDefineRepository.findById(ifCode).map(PayConverter::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public boolean updateIfDefine(PayInterfaceDefineDTO dto) {
        return payInterfaceDefineRepository.findById(dto.getIfCode()).map(entity -> {
            entity.setIfName(dto.getIfName());
            entity.setIfType(dto.getIfType());
            entity.setConfigPageType(dto.getConfigPageType());
            entity.setIcon(dto.getIcon());
            entity.setBgColor(dto.getBgColor());
            entity.setState(dto.getState());
            entity.setRemark(dto.getRemark());
            entity.setConfigInfo(dto.getConfigInfo());
            payInterfaceDefineRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    public List<PayInterfaceDefineDTO> listIfDefine(Byte state) {
        return PayConverter.toPayInterfaceDefineDTOList(payInterfaceDefineRepository.findByState(state));
    }

    @Override
    @Transactional
    public PayInterfaceConfigDTO saveIfConfig(PayInterfaceConfigDTO dto) {
        Optional<PayInterfaceConfig> opt = payInterfaceConfigRepository.findByInfoTypeAndInfoIdAndIfCode(
                dto.getInfoType(), dto.getInfoId(), dto.getIfCode());
        PayInterfaceConfig entity;
        if (opt.isPresent()) {
            entity = opt.get();
            entity.setIfParams(dto.getIfParams());
            entity.setState(dto.getState());
            entity.setRemark(dto.getRemark());
        } else {
            entity = PayConverter.toEntity(dto);
        }
        entity = payInterfaceConfigRepository.save(entity);
        return PayConverter.toDTO(entity);
    }

    @Override
    public PayInterfaceConfigDTO getIfConfig(String infoType, String infoId, String ifCode) {
        return payInterfaceConfigRepository.findByInfoTypeAndInfoIdAndIfCode(infoType, infoId, ifCode)
                .map(PayConverter::toDTO).orElse(null);
    }

    @Override
    public List<PayInterfaceConfigDTO> listIfConfig(String infoType, String infoId) {
        return PayConverter.toPayInterfaceConfigDTOList(payInterfaceConfigRepository.findByInfoTypeAndInfoId(infoType, infoId));
    }

    @Override
    @Transactional
    public boolean saveMchPayPassage(List<MchPayPassageDTO> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        MchPayPassageDTO first = list.get(0);
        mchPayPassageRepository.deleteByMchNoAndAppId(first.getMchNo(), first.getAppId());
        for (MchPayPassageDTO dto : list) {
            MchPayPassage entity = PayConverter.toEntity(dto);
            entity.setId(null);
            mchPayPassageRepository.save(entity);
        }
        return true;
    }

    @Override
    public List<MchPayPassageDTO> listMchPayPassage(String mchNo, String appId) {
        return PayConverter.toMchPayPassageDTOList(mchPayPassageRepository.findByMchNoAndAppId(mchNo, appId));
    }

    @Override
    public MchPayPassageDTO getAvailablePassage(String mchNo, String appId, String wayCode) {
        return mchPayPassageRepository.findByMchNoAndAppIdAndWayCodeAndState(mchNo, appId, wayCode, STATE_ACTIVE)
                .map(PayConverter::toDTO).orElse(null);
    }
}
