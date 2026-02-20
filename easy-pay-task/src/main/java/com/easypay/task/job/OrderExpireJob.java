package com.easypay.task.job;

import com.easypay.api.dto.pay.PayOrderDTO;
import com.easypay.api.result.PageResult;
import com.easypay.api.service.pay.IPayOrderService;
import com.easypay.task.config.TaskProperties;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 订单过期关闭任务
 * 扫描超过过期时间仍处于初始/支付中的订单，自动关闭
 */
@Component
@ConditionalOnProperty(prefix = "easypay.task", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OrderExpireJob {

    private static final Logger log = LoggerFactory.getLogger(OrderExpireJob.class);

    private static final byte STATE_INIT = 0;
    private static final byte STATE_ING = 1;
    private static final byte STATE_CLOSED = 6;

    @DubboReference
    private IPayOrderService payOrderService;

    @Resource
    private TaskProperties taskProperties;

    @Scheduled(cron = "${easypay.task.order-expire.cron}")
    public void execute() {
        int batchSize = taskProperties.getOrderExpire().getBatchSize();
        log.info("[OrderExpireJob] 开始执行, batchSize={}", batchSize);

        int closedCount = 0;
        try {
            closedCount += closeExpiredOrders(STATE_INIT, batchSize);
            closedCount += closeExpiredOrders(STATE_ING, batchSize);
        } catch (Exception e) {
            log.error("[OrderExpireJob] 执行异常", e);
        }

        log.info("[OrderExpireJob] 执行完成, 关闭订单数={}", closedCount);
    }

    private int closeExpiredOrders(byte currentState, int batchSize) {
        int closed = 0;
        PayOrderDTO query = new PayOrderDTO();
        query.setState(currentState);

        PageResult<PayOrderDTO> page = payOrderService.page(query, 1, batchSize);
        if (page == null || page.getRecords() == null) {
            return 0;
        }

        LocalDateTime now = LocalDateTime.now();
        for (PayOrderDTO order : page.getRecords()) {
            if (order.getExpiredTime() != null && order.getExpiredTime().isBefore(now)) {
                boolean ok = payOrderService.updateState(
                        order.getPayOrderId(), currentState, STATE_CLOSED, null);
                if (ok) {
                    closed++;
                    log.debug("[OrderExpireJob] 关闭过期订单: {}", order.getPayOrderId());
                }
            }
        }
        return closed;
    }
}
