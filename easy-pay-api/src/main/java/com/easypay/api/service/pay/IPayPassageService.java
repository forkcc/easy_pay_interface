package com.easypay.api.service.pay;

import com.easypay.api.dto.pay.MchPayPassageDTO;
import com.easypay.api.dto.pay.PayInterfaceConfigDTO;
import com.easypay.api.dto.pay.PayInterfaceDefineDTO;
import com.easypay.api.dto.pay.PayPassageDTO;
import com.easypay.api.dto.pay.PayWayDTO;

import java.util.List;

public interface IPayPassageService {

    PayPassageDTO createPassage(PayPassageDTO dto);

    PayPassageDTO getPassageById(Long passageId);

    boolean updatePassage(PayPassageDTO dto);

    List<PayPassageDTO> listPassage(Byte state);

    PayWayDTO createWay(PayWayDTO dto);

    PayWayDTO getWayByCode(String wayCode);

    boolean updateWay(PayWayDTO dto);

    List<PayWayDTO> listWay(Byte state);

    PayInterfaceDefineDTO createIfDefine(PayInterfaceDefineDTO dto);

    PayInterfaceDefineDTO getIfDefineByCode(String ifCode);

    boolean updateIfDefine(PayInterfaceDefineDTO dto);

    List<PayInterfaceDefineDTO> listIfDefine(Byte state);

    PayInterfaceConfigDTO saveIfConfig(PayInterfaceConfigDTO dto);

    PayInterfaceConfigDTO getIfConfig(String infoType, String infoId, String ifCode);

    List<PayInterfaceConfigDTO> listIfConfig(String infoType, String infoId);

    boolean saveMchPayPassage(List<MchPayPassageDTO> list);

    List<MchPayPassageDTO> listMchPayPassage(String mchNo, String appId);

    MchPayPassageDTO getAvailablePassage(String mchNo, String appId, String wayCode);
}
