package com.shinhan.pda_midterm_project.domain.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.shinhan.pda_midterm_project.common.config.JwtProperties;
import com.shinhan.pda_midterm_project.domain.auth.model.UserTokens;
import com.shinhan.pda_midterm_project.domain.auth.service.TokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.time.Clock;
import java.time.ZoneId;
import javax.crypto.SecretKey;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.system(ZoneId.of("Asia/Seoul"));
        SecretKey secretKey = Jwts.SIG.HS256.key().build();
        tokenProvider = new TokenProvider(jwtProperties, clock, secretKey);

    }


    @Test
    void 토큰을_생성한다() {
        // given
        Long expireMs = 60L;
        String subject = "testSubject";

        // when
        String createdToken = tokenProvider.createToken(subject, expireMs);

        // then
        assertNotNull(createdToken);
    }

    @Test
    void 토큰_검증을_성공한다() {
        // given
        Long expireMs = 100000L;
        String subject = "testSubject";
        String createdToken = tokenProvider.createToken(subject, expireMs);

        // when, then
        assertDoesNotThrow(() -> tokenProvider.verify(createdToken));
    }

    @Test
    void 유효시간이_지나면_토큰_검증을_실패한다() {
        // given
        Long expireMs = 0L;
        String subject = "testSubject";
        String createdToken = tokenProvider.createToken(subject, expireMs);

        // when, then
        assertThrows(
                ExpiredJwtException.class,
                () -> tokenProvider.verify(createdToken));
    }

    @Test
    void 유효한_토큰에서_subject를_가져온다() {
        // given
        Long expireMs = 100000L;
        String expectedSubject = "testSubject";
        String createdToken = tokenProvider.createToken(expectedSubject, expireMs);

        // when
        String actualSubject = tokenProvider.getSubject(createdToken);

        // then
        Assertions.assertThat(actualSubject).isEqualTo(expectedSubject);
    }

    @Test
    void acess_token과_refresh_token을_생성한다() {
        // given
        String memberId = "1L";
        UserTokens userTokens = tokenProvider.generateTokens(memberId);

        // then
        assertNotNull(userTokens);
    }
}
