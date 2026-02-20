package com.easypay.task.job;

import com.easypay.api.dto.account.SettRecordDTO;
import com.easypay.api.result.PageResult;
import com.easypay.api.service.account.ISettService;
import com.easypay.api.service.sys.ISysConfigService;
import com.easypay.api.dto.sys.SysConfigDTO;
import com.easypay.task.config.TaskProperties;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 自动结算任务
 * 每天凌晨 02:30 执行，处理所有状态为「结算中」的结算单
 * 实际打款逻辑需对接银行/三方渠道，此处仅做状态流转框架
 */
@Component
@ConditionalOnProperty(prefix = "easypay.task", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AutoSettleJob {

    private static final Logger log = LoggerFactory.getLogger(AutoSettleJob.class);

    private static final byte STATE_ING = 1;
    private static final byte STATE_SUCCESS = 2;
    private static final byte STATE_FAIL = 3;

    @DubboReference
    private ISettService settService;

    @DubboReference
    private ISysConfigService sysConfigService;

    @Resource
    private TaskProperties taskProperties;

    @Scheduled(cron = "${easypay.task.auto-settle.cron}")
    public void execute() {
        int batchSize = taskProperties.getAutoSettle().getBatchSize();
        log.info("[AutoSettleJob] 开始执行, batchSize={}", batchSize);

        int successCount = 0;
        int failCount = 0;
        int pageNum = 1;

        try {
            while (true) {
                PageResult<SettRecordDTO> page = settService.pageSettRecord(
                        null, STATE_ING, pageNum, batchSize);

                if (page == null || page.getRecords() == null || page.getRecords().isEmpty()) {
                    break;
                }

                for (SettRecordDTO record : page.getRecords()) {
                    boolean ok = processSettle(record);
                    if (ok) {
                        successCount++;
                    } else {
                        failCount++;
                    }
                }

                if (page.getRecords().size() < batchSize) {
                    break;
                }
                pageNum++;
            }
        } catch (Exception e) {
            log.error("[AutoSettleJob] 执行异常", e);
        }

        log.info("[AutoSettleJob] 执行完成, 成功={}, 失败={}", successCount, failCount);
    }

    private boolean processSettle(SettRecordDTO record) {
        try {
            // TODO: 对接银行/三方渠道执行实际打款
            // 此处为框架占位，默认标记为成功
            boolean ok = settService.updateSettState(record.getId(), STATE_SUCCESS, null);
            if (ok) {
                log.debug("[AutoSettleJob] 结算成功, settNo={}, amount={}",
                        record.getSettNo(), record.getRemitAmount());
            }
            return ok;
        } catch (Exception e) {
            log.error("[AutoSettleJob] 结算处理异常, settNo={}", record.getSettNo(), e);
            settService.updateSettState(record.getId(), STATE_FAIL, e.getMessage());
            return false;
        }
    }
}
