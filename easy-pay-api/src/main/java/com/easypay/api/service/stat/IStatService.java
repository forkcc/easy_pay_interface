package com.easypay.api.service.stat;

import com.easypay.api.dto.stat.OrderStatDailyDTO;
import com.easypay.api.dto.stat.TradeStatDTO;
import java.time.LocalDate;
import java.util.List;

/**
 * 统计服务接口
 */
public interface IStatService {

    // 按日期聚合统计数据
    boolean aggregateDaily(LocalDate date);

    // 查询每日订单统计数据
    List<OrderStatDailyDTO> queryDailyStat(LocalDate startDate, LocalDate endDate, String mchNo, String agentNo, String wayCode, Long passageId);

    // 查询指定日期范围内的交易汇总
    TradeStatDTO summaryBetween(LocalDate startDate, LocalDate endDate, String mchNo, String agentNo);
}
