package com.shinhan.pda_midterm_project.domain.auth.service;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.exception.TokenException;
import jakarta.servlet.http.Cookie;
import java.time.Duration;
import java.util.Arrays;
import org.springframework.http.ResponseCookie;

public class TokenCookieManager {
    private static final String TOKEN_NAME = "access-token";
    private static final Duration COOKIE_MAX_AGE = Duration.ofDays(14L);

    public static String extractToken(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_NAME))
                .findFirst()
                .orElseThrow(() -> new TokenException(ResponseMessages.ACCESS_TOKEN_NOT_FOUND))
                .getValue();
    }

    public static ResponseCookie createCookie(String tokenValue) {
        return ResponseCookie.from(TOKEN_NAME, tokenValue)
                .maxAge(COOKIE_MAX_AGE)
                .secure(false)
                .httpOnly(false)
                .sameSite("none")
                .path("/")
                .build();
    }

    public static ResponseCookie deleteCookie() {
        return ResponseCookie.from(TOKEN_NAME, "")
                .path("/")
                .maxAge(0)
                .secure(false)
                .httpOnly(true)
                .sameSite("none")
                .build();
    }

}
