package com.shinhan.pda_midterm_project.domain.member.exception;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
    private final ResponseMessages responseMessage;

    public MemberException(ResponseMessages responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getCode() {
        return responseMessage.getCode();
    }
}
