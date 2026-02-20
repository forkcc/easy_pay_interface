package com.easypay.provider.repository.pay;

import com.easypay.provider.entity.pay.PayPassage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayPassageRepository extends JpaRepository<PayPassage, Long> {

    List<PayPassage> findByState(Byte state);
}
