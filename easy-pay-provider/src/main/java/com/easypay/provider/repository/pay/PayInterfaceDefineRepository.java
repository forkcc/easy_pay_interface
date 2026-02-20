package com.easypay.provider.repository.pay;

import com.easypay.provider.entity.pay.PayInterfaceDefine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayInterfaceDefineRepository extends JpaRepository<PayInterfaceDefine, String> {

    List<PayInterfaceDefine> findByState(Byte state);
}
