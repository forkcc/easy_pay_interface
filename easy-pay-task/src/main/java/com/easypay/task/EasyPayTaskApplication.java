package com.easypay.task;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务模块启动类
 *
 * <p>以非 Web 方式运行，通过 Dubbo 远程调用 Provider 服务，
 * 执行订单过期关闭、通知重试、每日统计聚合、自动结算等定时任务。</p>
 */
@SpringBootApplication
@EnableDubbo
@EnableScheduling
public class EasyPayTaskApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(EasyPayTaskApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
