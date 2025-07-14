package com.shinhan.pda_midterm_project.common.resolver;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.exception.AuthException;
import com.shinhan.pda_midterm_project.domain.auth.exception.TokenException;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.auth.service.TokenCookieManager;
import com.shinhan.pda_midterm_project.domain.auth.service.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthenticationResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;
    private final TokenCookieManager tokenCookieManager;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null) {
            throw new AuthException(ResponseMessages.AUTH_BAD_REQUEST);
        }

        try {
            String accessTokenValue = tokenCookieManager.extractToken(request.getCookies());
            tokenProvider.verify(accessTokenValue);

            Long memberId = Long.valueOf(tokenProvider.getSubject(accessTokenValue));
            return Accessor.member(memberId);
        } catch (TokenException e) {
            return Accessor.guest();
        }
    }
}
