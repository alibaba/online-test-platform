package com.alibaba.test.steed.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by liyang on 2019/8/31.
 */
@Configuration
@EnableAsync
public class ExecutorConfig {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);

    @Bean
    public Executor asyncServiceExecutor() {

        logger.info("Start Async Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);

        executor.setMaxPoolSize(5);

        executor.setThreadNamePrefix("smoke-service-");

        executor.setQueueCapacity(99999);

        executor.initialize();

        return executor;
    }
}
