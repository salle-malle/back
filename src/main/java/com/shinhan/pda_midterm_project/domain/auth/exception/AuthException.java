package com.shinhan.pda_midterm_project.domain.auth.exception;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private final ResponseMessages responseMessage;

    public AuthException(ResponseMessages responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getCode() {
        return responseMessage.getCode();
    }
}
