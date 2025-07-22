package com.shinhan.pda_midterm_project.domain.auth;

import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.exception.AuthException;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MemberOnlyChecker {
    @Before("@annotation(com.shinhan.pda_midterm_project.common.annotation.MemberOnly)")
    public void check(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Accessor accessor && !accessor.isMember()) {
                throw new AuthException(ResponseMessages.AUTH_BAD_REQUEST);
            }
        }
    }
}
