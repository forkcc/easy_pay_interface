package com.easypay.provider.converter;

import com.easypay.api.dto.mch.MchAppDTO;
import com.easypay.api.dto.mch.MchInfoDTO;
import com.easypay.api.dto.mch.MchNotifyDTO;
import com.easypay.provider.entity.mch.MchApp;
import com.easypay.provider.entity.mch.MchInfo;
import com.easypay.provider.entity.mch.MchNotify;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MchConverter {

    public static MchInfoDTO toDTO(MchInfo entity) {
        if (entity == null) {
            return null;
        }
        MchInfoDTO dto = new MchInfoDTO();
        dto.setMchNo(entity.getMchNo());
        dto.setMchName(entity.getMchName());
        dto.setMchShortName(entity.getMchShortName());
        dto.setType(entity.getType());
        dto.setState(entity.getState());
        dto.setAgentNo(entity.getAgentNo());
        dto.setContactName(entity.getContactName());
        dto.setContactTel(entity.getContactTel());
        dto.setContactEmail(entity.getContactEmail());
        dto.setApiKey(entity.getApiKey());
        dto.setRemark(entity.getRemark());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static MchInfo toEntity(MchInfoDTO dto) {
        if (dto == null) {
            return null;
        }
        MchInfo entity = new MchInfo();
        entity.setMchNo(dto.getMchNo());
        entity.setMchName(dto.getMchName());
        entity.setMchShortName(dto.getMchShortName());
        entity.setType(dto.getType());
        entity.setState(dto.getState());
        entity.setAgentNo(dto.getAgentNo());
        entity.setContactName(dto.getContactName());
        entity.setContactTel(dto.getContactTel());
        entity.setContactEmail(dto.getContactEmail());
        entity.setApiKey(dto.getApiKey());
        entity.setRemark(dto.getRemark());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<MchInfoDTO> toMchInfoDTOList(List<MchInfo> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<MchInfoDTO> result = new ArrayList<>(entities.size());
        for (MchInfo entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static MchAppDTO toDTO(MchApp entity) {
        if (entity == null) {
            return null;
        }
        MchAppDTO dto = new MchAppDTO();
        dto.setAppId(entity.getAppId());
        dto.setMchNo(entity.getMchNo());
        dto.setAppName(entity.getAppName());
        dto.setState(entity.getState());
        dto.setAppSecret(entity.getAppSecret());
        dto.setNotifyUrl(entity.getNotifyUrl());
        dto.setReturnUrl(entity.getReturnUrl());
        dto.setRemark(entity.getRemark());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static MchApp toEntity(MchAppDTO dto) {
        if (dto == null) {
            return null;
        }
        MchApp entity = new MchApp();
        entity.setAppId(dto.getAppId());
        entity.setMchNo(dto.getMchNo());
        entity.setAppName(dto.getAppName());
        entity.setState(dto.getState());
        entity.setAppSecret(dto.getAppSecret());
        entity.setNotifyUrl(dto.getNotifyUrl());
        entity.setReturnUrl(dto.getReturnUrl());
        entity.setRemark(dto.getRemark());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<MchAppDTO> toMchAppDTOList(List<MchApp> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<MchAppDTO> result = new ArrayList<>(entities.size());
        for (MchApp entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public static MchNotifyDTO toDTO(MchNotify entity) {
        if (entity == null) {
            return null;
        }
        MchNotifyDTO dto = new MchNotifyDTO();
        dto.setNotifyId(entity.getNotifyId());
        dto.setOrderId(entity.getOrderId());
        dto.setOrderType(entity.getOrderType());
        dto.setMchNo(entity.getMchNo());
        dto.setMchOrderNo(entity.getMchOrderNo());
        dto.setNotifyUrl(entity.getNotifyUrl());
        dto.setState(entity.getState());
        dto.setNotifyCount(entity.getNotifyCount());
        dto.setNotifyCountLimit(entity.getNotifyCountLimit());
        dto.setLastNotifyTime(entity.getLastNotifyTime());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static MchNotify toEntity(MchNotifyDTO dto) {
        if (dto == null) {
            return null;
        }
        MchNotify entity = new MchNotify();
        entity.setNotifyId(dto.getNotifyId());
        entity.setOrderId(dto.getOrderId());
        entity.setOrderType(dto.getOrderType());
        entity.setMchNo(dto.getMchNo());
        entity.setMchOrderNo(dto.getMchOrderNo());
        entity.setNotifyUrl(dto.getNotifyUrl());
        entity.setState(dto.getState());
        entity.setNotifyCount(dto.getNotifyCount());
        entity.setNotifyCountLimit(dto.getNotifyCountLimit());
        entity.setLastNotifyTime(dto.getLastNotifyTime());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static List<MchNotifyDTO> toMchNotifyDTOList(List<MchNotify> entities) {
        if (entities == null) {
            return null;
        }
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<MchNotifyDTO> result = new ArrayList<>(entities.size());
        for (MchNotify entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }
}
