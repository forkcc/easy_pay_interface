package com.easypay.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableDubbo
public class EasyPayProviderApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(EasyPayProviderApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
