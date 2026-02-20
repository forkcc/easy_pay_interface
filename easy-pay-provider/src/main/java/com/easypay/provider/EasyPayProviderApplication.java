package com.easypay.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 支付服务提供者启动类，以非 Web 模式运行并启用 Dubbo 服务暴露
 */
@SpringBootApplication
@EnableDubbo
public class EasyPayProviderApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(EasyPayProviderApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
