package com.easypay.provider.repository.pay;

import com.easypay.provider.entity.pay.PayInterfaceDefine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 支付接口定义数据访问接口，对应实体 {@link PayInterfaceDefine}。
 */
public interface PayInterfaceDefineRepository extends JpaRepository<PayInterfaceDefine, String> {

    List<PayInterfaceDefine> findByState(Byte state);
}
