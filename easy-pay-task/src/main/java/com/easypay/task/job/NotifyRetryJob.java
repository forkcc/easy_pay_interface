package com.easypay.task.job;

import com.easypay.api.dto.mch.MchNotifyDTO;
import com.easypay.api.service.mch.IMchNotifyService;
import com.easypay.task.config.TaskProperties;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

/**
 * 商户通知重试任务
 * 拉取待通知记录，发起 HTTP 回调，根据响应更新通知状态
 */
@Component
@ConditionalOnProperty(prefix = "easypay.task", name = "enabled", havingValue = "true", matchIfMissing = true)
public class NotifyRetryJob {

    private static final Logger log = LoggerFactory.getLogger(NotifyRetryJob.class);

    private static final byte STATE_ING = 1;
    private static final byte STATE_SUCCESS = 2;
    private static final byte STATE_FAIL = 3;
    private static final String SUCCESS_RESPONSE = "success";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    @DubboReference
    private IMchNotifyService mchNotifyService;

    @Resource
    private TaskProperties taskProperties;

    @Scheduled(cron = "${easypay.task.notify-retry.cron}")
    public void execute() {
        int batchSize = taskProperties.getNotifyRetry().getBatchSize();
        log.info("[NotifyRetryJob] 开始执行, batchSize={}", batchSize);

        int successCount = 0;
        int failCount = 0;

        try {
            List<MchNotifyDTO> pendingList = mchNotifyService.listPendingNotify(batchSize);
            if (pendingList == null || pendingList.isEmpty()) {
                log.info("[NotifyRetryJob] 无待通知记录");
                return;
            }

            for (MchNotifyDTO notify : pendingList) {
                if (notify.getNotifyCountLimit() != null
                        && notify.getNotifyCount() >= notify.getNotifyCountLimit()) {
                    mchNotifyService.updateState(notify.getNotifyId(), STATE_FAIL,
                            notify.getNotifyCount());
                    failCount++;
                    continue;
                }

                boolean ok = doNotify(notify);
                int newCount = (notify.getNotifyCount() == null ? 0 : notify.getNotifyCount()) + 1;

                if (ok) {
                    mchNotifyService.updateState(notify.getNotifyId(), STATE_SUCCESS, newCount);
                    successCount++;
                } else {
                    mchNotifyService.updateState(notify.getNotifyId(), STATE_ING, newCount);
                    failCount++;
                }
            }
        } catch (Exception e) {
            log.error("[NotifyRetryJob] 执行异常", e);
        }

        log.info("[NotifyRetryJob] 执行完成, 成功={}, 失败={}", successCount, failCount);
    }

    private boolean doNotify(MchNotifyDTO notify) {
        if (notify.getNotifyUrl() == null || notify.getNotifyUrl().isBlank()) {
            log.warn("[NotifyRetryJob] 通知地址为空, notifyId={}", notify.getNotifyId());
            return false;
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(notify.getNotifyUrl()))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(buildNotifyBody(notify)))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            boolean ok = response.statusCode() == 200
                    && SUCCESS_RESPONSE.equalsIgnoreCase(response.body().trim());

            log.debug("[NotifyRetryJob] notifyId={}, url={}, status={}, ok={}",
                    notify.getNotifyId(), notify.getNotifyUrl(), response.statusCode(), ok);
            return ok;
        } catch (Exception e) {
            log.warn("[NotifyRetryJob] HTTP通知失败, notifyId={}, url={}, error={}",
                    notify.getNotifyId(), notify.getNotifyUrl(), e.getMessage());
            return false;
        }
    }

    private String buildNotifyBody(MchNotifyDTO notify) {
        return "{\"orderId\":\"" + notify.getOrderId()
                + "\",\"orderType\":\"" + notify.getOrderType()
                + "\",\"mchNo\":\"" + notify.getMchNo()
                + "\",\"mchOrderNo\":\"" + notify.getMchOrderNo() + "\"}";
    }
}
