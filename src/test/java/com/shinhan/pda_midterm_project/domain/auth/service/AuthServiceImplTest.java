package com.shinhan.pda_midterm_project.domain.auth.service;

import static org.mockito.ArgumentMatchers.any;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.exception.AuthException;
import com.shinhan.pda_midterm_project.domain.auth.model.AccessToken;
import com.shinhan.pda_midterm_project.domain.auth.model.UserTokens;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private MemberService memberService;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void 로그인을_성공하면_토큰을_발급한다() {
        // given
        String id = "testId";
        String password = "testPassword";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Member member = new Member(
                1L,
                null,
                id,
                hashedPassword,
                null, null, null, null, null, null, null, null);

        UserTokens token = UserTokens.of(AccessToken.of("token"));

        Mockito.when(memberService.findByMemberId(any()))
                .thenReturn(member);
        Mockito.when((tokenProvider.generateTokens(any()))).thenReturn(token);

        // when
        UserTokens loginResult = authService.login(id, password);

        // then
        Assertions.assertNotNull(loginResult.accessToken());
    }

    @Test
    void 로그인을_실패한다() {
        // given
        String id = "testId";
        String password = "testPassword";
        String hashedPassword = BCrypt.hashpw("differentPassword", BCrypt.gensalt());

        Member member = new Member(
                1L,
                null,
                id,
                hashedPassword,
                null, null, null, null, null, null, null, null);

        Mockito.when(memberService.findByMemberId(any()))
                .thenReturn(member);

        // when
        AuthException exception = Assertions.assertThrows(AuthException.class, () -> {
            authService.login(id, password);
        });

        // then
        Assertions.assertEquals(ResponseMessages.LOGIN_FAIL.getCode(), exception.getCode());
    }
}
