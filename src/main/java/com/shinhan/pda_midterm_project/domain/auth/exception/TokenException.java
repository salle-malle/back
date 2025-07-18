package com.shinhan.pda_midterm_project.domain.auth.exception;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import lombok.Getter;

@Getter
public class TokenException extends AuthException {
    private final ResponseMessages responseMessage;

    public TokenException(ResponseMessages responseMessage) {
        super(responseMessage);
        this.responseMessage = responseMessage;
    }
}
