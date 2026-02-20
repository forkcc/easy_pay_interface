package com.easypay.api.service.stat;

import com.easypay.api.dto.stat.OrderStatDailyDTO;
import com.easypay.api.dto.stat.TradeStatDTO;
import java.time.LocalDate;
import java.util.List;

public interface IStatService {

    boolean aggregateDaily(LocalDate date);

    List<OrderStatDailyDTO> queryDailyStat(LocalDate startDate, LocalDate endDate, String mchNo, String agentNo, String wayCode, Long passageId);

    TradeStatDTO summaryBetween(LocalDate startDate, LocalDate endDate, String mchNo, String agentNo);
}
