package com.shinhan.pda_midterm_project.domain.disclosure.exception;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;

public class DisclosureException extends RuntimeException {
    private final ResponseMessages responseMessage;

    public DisclosureException(ResponseMessages responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getCode() {
        return responseMessage.getCode();
    }
}
