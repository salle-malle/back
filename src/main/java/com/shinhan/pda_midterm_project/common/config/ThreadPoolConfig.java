package com.shinhan.pda_midterm_project.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ThreadPoolConfig {

    @Bean(name = "crawlingExecutor")
    public Executor crawlingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);   // 기본 스레드 수
        executor.setMaxPoolSize(10);   // 최대 스레드 수
        executor.setQueueCapacity(25); // 큐 용량
        executor.setThreadNamePrefix("crawling-thread-");
        executor.initialize();
        return executor;
    }
}