package com.shinhan.pda_midterm_project.domain.member_stock.exception;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import lombok.Getter;

@Getter
public class MemberStockException extends RuntimeException {
    private final ResponseMessages responseMessage;

    public MemberStockException(ResponseMessages responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getCode() {
        return responseMessage.getCode();
    }
}
