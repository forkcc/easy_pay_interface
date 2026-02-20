package com.easypay.provider.service.impl.stat;

import com.easypay.api.dto.stat.OrderStatDailyDTO;
import com.easypay.api.dto.stat.TradeStatDTO;
import com.easypay.api.service.stat.IStatService;
import com.easypay.provider.converter.AccountConverter;
import com.easypay.provider.entity.stat.OrderStatDaily;
import com.easypay.provider.repository.stat.OrderStatDailyRepository;
import com.easypay.provider.repository.pay.PayOrderRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@DubboService
public class StatServiceImpl implements IStatService {

    @Resource
    private OrderStatDailyRepository orderStatDailyRepository;

    @Resource
    private PayOrderRepository payOrderRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public boolean aggregateDaily(LocalDate date) {
        try {
            orderStatDailyRepository.deleteByStatDate(date);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            String sql = "SELECT mch_no, way_code, COUNT(*), COALESCE(SUM(amount),0), COALESCE(SUM(fee_amount),0), SUM(CASE WHEN state=2 THEN 1 ELSE 0 END), SUM(CASE WHEN state=2 THEN amount ELSE 0 END) FROM t_pay_order WHERE created_at >= :start AND created_at <= :end GROUP BY mch_no, way_code";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("start", startOfDay);
            query.setParameter("end", endOfDay);
            @SuppressWarnings("unchecked")
            List<Object[]> rows = query.getResultList();
            List<OrderStatDaily> entities = new ArrayList<>();
            for (Object[] row : rows) {
                OrderStatDaily stat = new OrderStatDaily();
                stat.setStatDate(date);
                stat.setMchNo((String) row[0]);
                stat.setWayCode((String) row[1]);
                stat.setOrderCount(((Number) row[2]).intValue());
                stat.setOrderAmount(((Number) row[3]).longValue());
                stat.setFeeAmount(((Number) row[4]).longValue());
                stat.setSuccessCount(((Number) row[5]).intValue());
                stat.setSuccessAmount(((Number) row[6]).longValue());
                entities.add(stat);
            }
            orderStatDailyRepository.saveAll(entities);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<OrderStatDailyDTO> queryDailyStat(LocalDate startDate, LocalDate endDate, String mchNo, String agentNo, String wayCode, Long passageId) {
        List<OrderStatDaily> list = orderStatDailyRepository.queryByCriteria(startDate, endDate, mchNo, agentNo, wayCode, passageId);
        return AccountConverter.toOrderStatDailyDTOList(list);
    }

    @Override
    public TradeStatDTO summaryBetween(LocalDate startDate, LocalDate endDate, String mchNo, String agentNo) {
        List<OrderStatDaily> list = orderStatDailyRepository.queryByCriteria(startDate, endDate, mchNo, agentNo, null, null);
        TradeStatDTO result = new TradeStatDTO();
        result.setStartDate(startDate);
        result.setEndDate(endDate);
        result.setMchNo(mchNo);
        result.setAgentNo(agentNo);
        int totalCount = 0;
        long totalAmount = 0L;
        long totalFee = 0L;
        int successCount = 0;
        long successAmount = 0L;
        for (OrderStatDaily stat : list) {
            totalCount += stat.getOrderCount() != null ? stat.getOrderCount() : 0;
            totalAmount += stat.getOrderAmount() != null ? stat.getOrderAmount() : 0L;
            totalFee += stat.getFeeAmount() != null ? stat.getFeeAmount() : 0L;
            successCount += stat.getSuccessCount() != null ? stat.getSuccessCount() : 0;
            successAmount += stat.getSuccessAmount() != null ? stat.getSuccessAmount() : 0L;
        }
        result.setTotalCount(totalCount);
        result.setTotalAmount(totalAmount);
        result.setTotalFee(totalFee);
        result.setSuccessCount(successCount);
        result.setSuccessAmount(successAmount);
        return result;
    }
}
