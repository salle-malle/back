package com.shinhan.pda_midterm_project.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String DATA_SERVER_BASE_URL = "http://127.0.0.1:8000";

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(DATA_SERVER_BASE_URL)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(50 * 1024 * 1024)) // 50MB로 확장
                .build();
    }
}
