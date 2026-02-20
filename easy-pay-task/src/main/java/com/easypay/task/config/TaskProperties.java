package com.easypay.task.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "easypay.task")
public class TaskProperties {

    private boolean enabled = true;

    private OrderExpire orderExpire = new OrderExpire();
    private NotifyRetry notifyRetry = new NotifyRetry();
    private DailyStat dailyStat = new DailyStat();
    private AutoSettle autoSettle = new AutoSettle();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public OrderExpire getOrderExpire() {
        return orderExpire;
    }

    public void setOrderExpire(OrderExpire orderExpire) {
        this.orderExpire = orderExpire;
    }

    public NotifyRetry getNotifyRetry() {
        return notifyRetry;
    }

    public void setNotifyRetry(NotifyRetry notifyRetry) {
        this.notifyRetry = notifyRetry;
    }

    public DailyStat getDailyStat() {
        return dailyStat;
    }

    public void setDailyStat(DailyStat dailyStat) {
        this.dailyStat = dailyStat;
    }

    public AutoSettle getAutoSettle() {
        return autoSettle;
    }

    public void setAutoSettle(AutoSettle autoSettle) {
        this.autoSettle = autoSettle;
    }

    public static class OrderExpire {
        private String cron = "0 */5 * * * ?";
        private int batchSize = 200;

        public String getCron() {
            return cron;
        }

        public void setCron(String cron) {
            this.cron = cron;
        }

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }
    }

    public static class NotifyRetry {
        private String cron = "0 */1 * * * ?";
        private int batchSize = 100;

        public String getCron() {
            return cron;
        }

        public void setCron(String cron) {
            this.cron = cron;
        }

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }
    }

    public static class DailyStat {
        private String cron = "0 5 0 * * ?";

        public String getCron() {
            return cron;
        }

        public void setCron(String cron) {
            this.cron = cron;
        }
    }

    public static class AutoSettle {
        private String cron = "0 30 2 * * ?";
        private int batchSize = 100;

        public String getCron() {
            return cron;
        }

        public void setCron(String cron) {
            this.cron = cron;
        }

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }
    }
}
