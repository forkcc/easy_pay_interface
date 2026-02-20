package com.easypay.task.job;

import com.easypay.api.service.stat.IStatService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 每日订单统计聚合任务
 * 每天凌晨 00:05 执行，将前一天的 t_pay_order 数据聚合到 t_order_stat_daily
 */
@Component
@ConditionalOnProperty(prefix = "easypay.task", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DailyStatJob {

    private static final Logger log = LoggerFactory.getLogger(DailyStatJob.class);

    @DubboReference
    private IStatService statService;

    @Scheduled(cron = "${easypay.task.daily-stat.cron}")
    public void execute() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("[DailyStatJob] 开始聚合, date={}", yesterday);

        try {
            boolean ok = statService.aggregateDaily(yesterday);
            if (ok) {
                log.info("[DailyStatJob] 聚合完成, date={}", yesterday);
            } else {
                log.warn("[DailyStatJob] 聚合返回失败, date={}", yesterday);
            }
        } catch (Exception e) {
            log.error("[DailyStatJob] 聚合异常, date={}", yesterday, e);
        }
    }
}
