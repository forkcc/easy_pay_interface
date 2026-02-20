package com.easypay.provider.repository.stat;

import com.easypay.provider.entity.stat.OrderStatDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderStatDailyRepository extends JpaRepository<OrderStatDaily, Long> {

    List<OrderStatDaily> findByStatDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT o FROM OrderStatDaily o WHERE o.statDate BETWEEN :start AND :end AND (:mchNo IS NULL OR o.mchNo = :mchNo) AND (:agentNo IS NULL OR o.agentNo = :agentNo) AND (:wayCode IS NULL OR o.wayCode = :wayCode) AND (:passageId IS NULL OR o.passageId = :passageId)")
    List<OrderStatDaily> queryByCriteria(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("mchNo") String mchNo, @Param("agentNo") String agentNo, @Param("wayCode") String wayCode, @Param("passageId") Long passageId);

    void deleteByStatDate(LocalDate statDate);
}
