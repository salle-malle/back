package com.shinhan.pda_midterm_project.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${fastapi.base-url}")
    private String fastApiBaseUrl;

    @Bean
    public WebClient fastApiWebClient() {
        return WebClient.builder()
                .baseUrl(fastApiBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}