package com.shinhan.pda_midterm_project.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtProperties {

    @Value("${jwt.access-expire-ms}")
    private Long accessExpireMs;

    @Value("${jwt.issuer}")
    private String issuer;
}

