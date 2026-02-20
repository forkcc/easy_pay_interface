package com.easypay.provider.converter;

import com.easypay.api.dto.pay.MchPayPassageDTO;
import com.easypay.api.dto.pay.PayInterfaceConfigDTO;
import com.easypay.api.dto.pay.PayInterfaceDefineDTO;
import com.easypay.api.dto.pay.PayOrderDTO;
import com.easypay.api.dto.pay.PayPassageDTO;
import com.easypay.api.dto.pay.PayWayDTO;
import com.easypay.api.dto.pay.RefundOrderDTO;
import com.easypay.api.dto.pay.TransferOrderDTO;
import com.easypay.provider.entity.pay.MchPayPassage;
import com.easypay.provider.entity.pay.PayInterfaceConfig;
import com.easypay.provider.entity.pay.PayInterfaceDefine;
import com.easypay.provider.entity.pay.PayOrder;
import com.easypay.provider.entity.pay.PayPassage;
import com.easypay.provider.entity.pay.PayWay;
import com.easypay.provider.entity.pay.RefundOrder;
import com.easypay.provider.entity.pay.TransferOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class PayConverter {

    public static PayOrderDTO toDTO(PayOrder entity) {
        if (entity == null) {
            return null;
        }
        PayOrderDTO dto = new PayOrderDTO();
        dto.setPayOrderId(entity.getPayOrderId());
        dto.setMchNo(entity.getMchNo());
        dto.setAppId(entity.getAppId());
        dto.setMchOrderNo(entity.getMchOrderNo());
        dto.setWayCode(entity.getWayCode());
        dto.setAmount(entity.getAmount());
        dto.setCurrency(entity.getCurrency());
        dto.setState(entity.getState());
        dto.setClientIp(entity.getClientIp());
        dto.setSubject(entity.getSubject());
        dto.setBody(entity.getBody());
        dto.setFeeRate(entity.getFeeRate());
        dto.setFeeAmount(entity.getFeeAmount());
        dto.setChannelOrderNo(entity.getChannelOrderNo());
        dto.setChannelExtra(entity.getChannelExtra());
        dto.setNotifyUrl(entity.getNotifyUrl());
        dto.setReturnUrl(entity.getReturnUrl());
        dto.setErrCode(entity.getErrCode());
        dto.setErrMsg(entity.getErrMsg());
        dto.setExtParam(entity.getExtParam());
        dto.setExpiredTime(entity.getExpiredTime());
        dto.setSuccessTime(entity.getSuccessTime());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static PayOrder toEntity(PayOrderDTO dto) {
        if (dto == null) {
            return null;
        }
        PayOrder entity = new PayOrder();
        entity.setPayOrderId(dto.getPayOrderId());
        entity.setMchNo(dto.getMchNo());
        entity.setAppId(dto.getAppId());
        entity.setMchOrderNo(dto.getMchOrderNo());
        entity.setWayCode(dto.getWayCode());
        entity.setAmount(dto.getAmount());
        entity.setCurrency(dto.getCurrency());
        entity.setState(dto.getState());
        entity.setClientIp(dto.getClientIp());
        entity.setSubject(dto.getSubject());
        entity.setBody(dto.getBody());
        entity.setFeeRate(dto.getFeeRate());
        entity.setFeeAmount(dto.getFeeAmount());
        entity.setChannelOrderNo(dto.getChannelOrderNo());
        entity.setChannelExtra(dto.getChannelExtra());
        entity.setNotifyUrl(dto.getNotifyUrl());
        entity.setReturnUrl(dto.getReturnUrl());
        entity.setErrCode(dto.getErrCode());
        entity.setErrMsg(dto.getErrMsg());
        entity.setExtParam(dto.getExtParam());
        entity.setExpiredTime(dto.getExpiredTime());
        entity.setSuccessTime(dto.getSuccessTime());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<PayOrderDTO> toPayOrderDTOList(List<PayOrder> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<PayOrderDTO> result = new ArrayList<>(entities.size());
        for (PayOrder entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static RefundOrderDTO toDTO(RefundOrder entity) {
        if (entity == null) {
            return null;
        }
        RefundOrderDTO dto = new RefundOrderDTO();
        dto.setRefundOrderId(entity.getRefundOrderId());
        dto.setPayOrderId(entity.getPayOrderId());
        dto.setMchNo(entity.getMchNo());
        dto.setAppId(entity.getAppId());
        dto.setMchRefundNo(entity.getMchRefundNo());
        dto.setPayAmount(entity.getPayAmount());
        dto.setRefundAmount(entity.getRefundAmount());
        dto.setCurrency(entity.getCurrency());
        dto.setState(entity.getState());
        dto.setChannelOrderNo(entity.getChannelOrderNo());
        dto.setChannelRefundNo(entity.getChannelRefundNo());
        dto.setReason(entity.getReason());
        dto.setErrCode(entity.getErrCode());
        dto.setErrMsg(entity.getErrMsg());
        dto.setNotifyUrl(entity.getNotifyUrl());
        dto.setExtParam(entity.getExtParam());
        dto.setSuccessTime(entity.getSuccessTime());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static RefundOrder toEntity(RefundOrderDTO dto) {
        if (dto == null) {
            return null;
        }
        RefundOrder entity = new RefundOrder();
        entity.setRefundOrderId(dto.getRefundOrderId());
        entity.setPayOrderId(dto.getPayOrderId());
        entity.setMchNo(dto.getMchNo());
        entity.setAppId(dto.getAppId());
        entity.setMchRefundNo(dto.getMchRefundNo());
        entity.setPayAmount(dto.getPayAmount());
        entity.setRefundAmount(dto.getRefundAmount());
        entity.setCurrency(dto.getCurrency());
        entity.setState(dto.getState());
        entity.setChannelOrderNo(dto.getChannelOrderNo());
        entity.setChannelRefundNo(dto.getChannelRefundNo());
        entity.setReason(dto.getReason());
        entity.setErrCode(dto.getErrCode());
        entity.setErrMsg(dto.getErrMsg());
        entity.setNotifyUrl(dto.getNotifyUrl());
        entity.setExtParam(dto.getExtParam());
        entity.setSuccessTime(dto.getSuccessTime());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<RefundOrderDTO> toRefundOrderDTOList(List<RefundOrder> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<RefundOrderDTO> result = new ArrayList<>(entities.size());
        for (RefundOrder entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static TransferOrderDTO toDTO(TransferOrder entity) {
        if (entity == null) {
            return null;
        }
        TransferOrderDTO dto = new TransferOrderDTO();
        dto.setTransferId(entity.getTransferId());
        dto.setMchNo(entity.getMchNo());
        dto.setAppId(entity.getAppId());
        dto.setMchTransferNo(entity.getMchTransferNo());
        dto.setWayCode(entity.getWayCode());
        dto.setAmount(entity.getAmount());
        dto.setCurrency(entity.getCurrency());
        dto.setState(entity.getState());
        dto.setAccountNo(entity.getAccountNo());
        dto.setAccountName(entity.getAccountName());
        dto.setBankName(entity.getBankName());
        dto.setChannelOrderNo(entity.getChannelOrderNo());
        dto.setErrCode(entity.getErrCode());
        dto.setErrMsg(entity.getErrMsg());
        dto.setExtParam(entity.getExtParam());
        dto.setSuccessTime(entity.getSuccessTime());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static TransferOrder toEntity(TransferOrderDTO dto) {
        if (dto == null) {
            return null;
        }
        TransferOrder entity = new TransferOrder();
        entity.setTransferId(dto.getTransferId());
        entity.setMchNo(dto.getMchNo());
        entity.setAppId(dto.getAppId());
        entity.setMchTransferNo(dto.getMchTransferNo());
        entity.setWayCode(dto.getWayCode());
        entity.setAmount(dto.getAmount());
        entity.setCurrency(dto.getCurrency());
        entity.setState(dto.getState());
        entity.setAccountNo(dto.getAccountNo());
        entity.setAccountName(dto.getAccountName());
        entity.setBankName(dto.getBankName());
        entity.setChannelOrderNo(dto.getChannelOrderNo());
        entity.setErrCode(dto.getErrCode());
        entity.setErrMsg(dto.getErrMsg());
        entity.setExtParam(dto.getExtParam());
        entity.setSuccessTime(dto.getSuccessTime());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<TransferOrderDTO> toTransferOrderDTOList(List<TransferOrder> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<TransferOrderDTO> result = new ArrayList<>(entities.size());
        for (TransferOrder entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static PayPassageDTO toDTO(PayPassage entity) {
        if (entity == null) {
            return null;
        }
        PayPassageDTO dto = new PayPassageDTO();
        dto.setPassageId(entity.getPassageId());
        dto.setPassageName(entity.getPassageName());
        dto.setIfCode(entity.getIfCode());
        dto.setWayCode(entity.getWayCode());
        dto.setFeeRate(entity.getFeeRate());
        dto.setFeeAmount(entity.getFeeAmount());
        dto.setState(entity.getState());
        dto.setRemark(entity.getRemark());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static PayPassage toEntity(PayPassageDTO dto) {
        if (dto == null) {
            return null;
        }
        PayPassage entity = new PayPassage();
        entity.setPassageId(dto.getPassageId());
        entity.setPassageName(dto.getPassageName());
        entity.setIfCode(dto.getIfCode());
        entity.setWayCode(dto.getWayCode());
        entity.setFeeRate(dto.getFeeRate());
        entity.setFeeAmount(dto.getFeeAmount());
        entity.setState(dto.getState());
        entity.setRemark(dto.getRemark());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<PayPassageDTO> toPayPassageDTOList(List<PayPassage> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<PayPassageDTO> result = new ArrayList<>(entities.size());
        for (PayPassage entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static PayWayDTO toDTO(PayWay entity) {
        if (entity == null) {
            return null;
        }
        PayWayDTO dto = new PayWayDTO();
        dto.setWayCode(entity.getWayCode());
        dto.setWayName(entity.getWayName());
        dto.setState(entity.getState());
        dto.setRemark(entity.getRemark());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static PayWay toEntity(PayWayDTO dto) {
        if (dto == null) {
            return null;
        }
        PayWay entity = new PayWay();
        entity.setWayCode(dto.getWayCode());
        entity.setWayName(dto.getWayName());
        entity.setState(dto.getState());
        entity.setRemark(dto.getRemark());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }

    public static List<PayWayDTO> toPayWayDTOList(List<PayWay> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<PayWayDTO> result = new ArrayList<>(entities.size());
        for (PayWay entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static PayInterfaceDefineDTO toDTO(PayInterfaceDefine entity) {
        if (entity == null) {
            return null;
        }
        PayInterfaceDefineDTO dto = new PayInterfaceDefineDTO();
        dto.setIfCode(entity.getIfCode());
        dto.setIfName(entity.getIfName());
        dto.setIfType(entity.getIfType());
        dto.setConfigPageType(entity.getConfigPageType());
        dto.setIcon(entity.getIcon());
        dto.setBgColor(entity.getBgColor());
        dto.setState(entity.getState());
        dto.setRemark(entity.getRemark());
        dto.setConfigInfo(entity.getConfigInfo());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static PayInterfaceDefine toEntity(PayInterfaceDefineDTO dto) {
        if (dto == null) {
            return null;
        }
        PayInterfaceDefine entity = new PayInterfaceDefine();
        entity.setIfCode(dto.getIfCode());
        entity.setIfName(dto.getIfName());
        entity.setIfType(dto.getIfType());
        entity.setConfigPageType(dto.getConfigPageType());
        entity.setIcon(dto.getIcon());
        entity.setBgColor(dto.getBgColor());
        entity.setState(dto.getState());
        entity.setRemark(dto.getRemark());
        entity.setConfigInfo(dto.getConfigInfo());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<PayInterfaceDefineDTO> toPayInterfaceDefineDTOList(List<PayInterfaceDefine> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<PayInterfaceDefineDTO> result = new ArrayList<>(entities.size());
        for (PayInterfaceDefine entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static PayInterfaceConfigDTO toDTO(PayInterfaceConfig entity) {
        if (entity == null) {
            return null;
        }
        PayInterfaceConfigDTO dto = new PayInterfaceConfigDTO();
        dto.setId(entity.getId());
        dto.setInfoType(entity.getInfoType());
        dto.setInfoId(entity.getInfoId());
        dto.setIfCode(entity.getIfCode());
        dto.setIfParams(entity.getIfParams());
        dto.setState(entity.getState());
        dto.setRemark(entity.getRemark());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static PayInterfaceConfig toEntity(PayInterfaceConfigDTO dto) {
        if (dto == null) {
            return null;
        }
        PayInterfaceConfig entity = new PayInterfaceConfig();
        entity.setId(dto.getId());
        entity.setInfoType(dto.getInfoType());
        entity.setInfoId(dto.getInfoId());
        entity.setIfCode(dto.getIfCode());
        entity.setIfParams(dto.getIfParams());
        entity.setState(dto.getState());
        entity.setRemark(dto.getRemark());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<PayInterfaceConfigDTO> toPayInterfaceConfigDTOList(List<PayInterfaceConfig> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<PayInterfaceConfigDTO> result = new ArrayList<>(entities.size());
        for (PayInterfaceConfig entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static MchPayPassageDTO toDTO(MchPayPassage entity) {
        if (entity == null) {
            return null;
        }
        MchPayPassageDTO dto = new MchPayPassageDTO();
        dto.setId(entity.getId());
        dto.setMchNo(entity.getMchNo());
        dto.setAppId(entity.getAppId());
        dto.setWayCode(entity.getWayCode());
        dto.setPassageId(entity.getPassageId());
        dto.setFeeRate(entity.getFeeRate());
        dto.setFeeAmount(entity.getFeeAmount());
        dto.setState(entity.getState());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static MchPayPassage toEntity(MchPayPassageDTO dto) {
        if (dto == null) {
            return null;
        }
        MchPayPassage entity = new MchPayPassage();
        entity.setId(dto.getId());
        entity.setMchNo(dto.getMchNo());
        entity.setAppId(dto.getAppId());
        entity.setWayCode(dto.getWayCode());
        entity.setPassageId(dto.getPassageId());
        entity.setFeeRate(dto.getFeeRate());
        entity.setFeeAmount(dto.getFeeAmount());
        entity.setState(dto.getState());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<MchPayPassageDTO> toMchPayPassageDTOList(List<MchPayPassage> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<MchPayPassageDTO> result = new ArrayList<>(entities.size());
        for (MchPayPassage entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }
}
