package com.shinhan.pda_midterm_project.domain.auth.service;

import com.shinhan.pda_midterm_project.common.config.JwtProperties;
import com.shinhan.pda_midterm_project.common.util.TimeUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private final JwtProperties jwtProperties;
    private final Clock clock;
    private final SecretKey secretKey;

    public String generateTokens(String subject) {
        return createToken(subject, jwtProperties.getAccessExpireMs());
    }

    public String createToken(String subject, Long expireMs) {
        Date now = TimeUtil.localDateTimeToDate(LocalDateTime.now(clock), clock);
        Date expiredTime = new Date(now.getTime() + expireMs);

        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(subject)
                .issuedAt(now)
                .signWith(secretKey)
                .expiration(expiredTime)
                .compact();
    }

    public Jws<Claims> verify(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }

    public String getSubject(String token) {
        return verify(token)
                .getPayload()
                .getSubject();
    }
}
