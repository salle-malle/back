package com.shinhan.pda_midterm_project.domain.auth.service;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.exception.TokenException;
import org.springframework.stereotype.Component;

@Component
public class TokenExtractor {
    private static final String TYPE = "Bearer ";

    public String extractToken(String headerValue) {
        if (headerValue == null || !headerValue.startsWith(TYPE)) {
            throw new TokenException(ResponseMessages.ACCESS_TOKEN_NOT_FOUND);
        }
        return headerValue.substring(TYPE.length());
    }
}
