package com.easypay.provider.service.impl.stat;

import com.easypay.api.dto.stat.OrderStatDailyDTO;
import com.easypay.api.dto.stat.TradeStatDTO;
import com.easypay.api.service.stat.IStatService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 统计服务实现，实现 {@link IStatService} 接口，提供物化视图刷新、每日交易统计查询及汇总功能
 */
@DubboService
public class StatServiceImpl implements IStatService {

    private static final Logger log = LoggerFactory.getLogger(StatServiceImpl.class);

    private static final String[] MATERIALIZED_VIEWS = {
            "mv_order_stat_daily",
            "mv_order_stat_mch",
            "mv_order_stat_way",
            "mv_order_stat_platform_daily"
    };

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public boolean aggregateDaily(LocalDate date) {
        try {
            for (String view : MATERIALIZED_VIEWS) {
                entityManager.createNativeQuery(
                        "REFRESH MATERIALIZED VIEW CONCURRENTLY " + view
                ).executeUpdate();
            }
            log.info("已刷新全部 {} 个物化视图（触发日期：{}）", MATERIALIZED_VIEWS.length, date);
            return true;
        } catch (Exception e) {
            log.error("刷新物化视图失败", e);
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OrderStatDailyDTO> queryDailyStat(LocalDate startDate, LocalDate endDate,
                                                   String mchNo, String agentNo,
                                                   String wayCode, Long passageId) {
        StringBuilder sql = new StringBuilder(
                "SELECT stat_date, mch_no, agent_no, way_code, order_count, order_amount, " +
                "fee_amount, success_count, success_amount FROM mv_order_stat_daily WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();
        int paramIdx = 1;

        if (startDate != null) {
            sql.append(" AND stat_date >= ?").append(paramIdx++);
            params.add(Date.valueOf(startDate));
        }
        if (endDate != null) {
            sql.append(" AND stat_date <= ?").append(paramIdx++);
            params.add(Date.valueOf(endDate));
        }
        if (mchNo != null && !mchNo.isEmpty()) {
            sql.append(" AND mch_no = ?").append(paramIdx++);
            params.add(mchNo);
        }
        if (agentNo != null && !agentNo.isEmpty()) {
            sql.append(" AND agent_no = ?").append(paramIdx++);
            params.add(agentNo);
        }
        if (wayCode != null && !wayCode.isEmpty()) {
            sql.append(" AND way_code = ?").append(paramIdx++);
            params.add(wayCode);
        }
        sql.append(" ORDER BY stat_date DESC, mch_no");

        var query = entityManager.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }

        List<Object[]> rows = query.getResultList();
        List<OrderStatDailyDTO> result = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            OrderStatDailyDTO dto = new OrderStatDailyDTO();
            dto.setStatDate(row[0] instanceof Date d ? d.toLocalDate() : ((java.sql.Date) row[0]).toLocalDate());
            dto.setMchNo((String) row[1]);
            dto.setAgentNo((String) row[2]);
            dto.setWayCode((String) row[3]);
            dto.setOrderCount(((Number) row[4]).intValue());
            dto.setOrderAmount(((Number) row[5]).longValue());
            dto.setFeeAmount(((Number) row[6]).longValue());
            dto.setSuccessCount(((Number) row[7]).intValue());
            dto.setSuccessAmount(((Number) row[8]).longValue());
            result.add(dto);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TradeStatDTO summaryBetween(LocalDate startDate, LocalDate endDate,
                                       String mchNo, String agentNo) {
        StringBuilder sql = new StringBuilder(
                "SELECT COALESCE(SUM(order_count),0), COALESCE(SUM(order_amount),0), " +
                "COALESCE(SUM(fee_amount),0), COALESCE(SUM(success_count),0), " +
                "COALESCE(SUM(success_amount),0) FROM mv_order_stat_daily WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();
        int paramIdx = 1;

        if (startDate != null) {
            sql.append(" AND stat_date >= ?").append(paramIdx++);
            params.add(Date.valueOf(startDate));
        }
        if (endDate != null) {
            sql.append(" AND stat_date <= ?").append(paramIdx++);
            params.add(Date.valueOf(endDate));
        }
        if (mchNo != null && !mchNo.isEmpty()) {
            sql.append(" AND mch_no = ?").append(paramIdx++);
            params.add(mchNo);
        }
        if (agentNo != null && !agentNo.isEmpty()) {
            sql.append(" AND agent_no = ?").append(paramIdx++);
            params.add(agentNo);
        }

        var query = entityManager.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }

        Object[] row = (Object[]) query.getSingleResult();
        TradeStatDTO result = new TradeStatDTO();
        result.setStartDate(startDate);
        result.setEndDate(endDate);
        result.setMchNo(mchNo);
        result.setAgentNo(agentNo);
        result.setTotalCount(((Number) row[0]).intValue());
        result.setTotalAmount(((Number) row[1]).longValue());
        result.setTotalFee(((Number) row[2]).longValue());
        result.setSuccessCount(((Number) row[3]).intValue());
        result.setSuccessAmount(((Number) row[4]).longValue());
        return result;
    }
}
