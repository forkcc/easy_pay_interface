package com.easypay.api.service.pay;

import com.easypay.api.dto.pay.MchPayPassageDTO;
import com.easypay.api.dto.pay.PayInterfaceConfigDTO;
import com.easypay.api.dto.pay.PayInterfaceDefineDTO;
import com.easypay.api.dto.pay.PayPassageDTO;
import com.easypay.api.dto.pay.PayWayDTO;

import java.util.List;

/**
 * 支付通道管理服务接口
 */
public interface IPayPassageService {

    // 创建支付通道
    PayPassageDTO createPassage(PayPassageDTO dto);

    // 根据通道ID查询支付通道
    PayPassageDTO getPassageById(Long passageId);

    // 更新支付通道
    boolean updatePassage(PayPassageDTO dto);

    // 按状态查询支付通道列表
    List<PayPassageDTO> listPassage(Byte state);

    // 创建支付方式
    PayWayDTO createWay(PayWayDTO dto);

    // 根据编码查询支付方式
    PayWayDTO getWayByCode(String wayCode);

    // 更新支付方式
    boolean updateWay(PayWayDTO dto);

    // 按状态查询支付方式列表
    List<PayWayDTO> listWay(Byte state);

    // 创建支付接口定义
    PayInterfaceDefineDTO createIfDefine(PayInterfaceDefineDTO dto);

    // 根据编码查询支付接口定义
    PayInterfaceDefineDTO getIfDefineByCode(String ifCode);

    // 更新支付接口定义
    boolean updateIfDefine(PayInterfaceDefineDTO dto);

    // 按状态查询支付接口定义列表
    List<PayInterfaceDefineDTO> listIfDefine(Byte state);

    // 保存支付接口配置
    PayInterfaceConfigDTO saveIfConfig(PayInterfaceConfigDTO dto);

    // 查询指定支付接口配置
    PayInterfaceConfigDTO getIfConfig(String infoType, String infoId, String ifCode);

    // 查询支付接口配置列表
    List<PayInterfaceConfigDTO> listIfConfig(String infoType, String infoId);

    // 批量保存商户支付通道
    boolean saveMchPayPassage(List<MchPayPassageDTO> list);

    // 查询商户支付通道列表
    List<MchPayPassageDTO> listMchPayPassage(String mchNo, String appId);

    // 获取可用的商户支付通道
    MchPayPassageDTO getAvailablePassage(String mchNo, String appId, String wayCode);
}
