package com.shinhan.pda_midterm_project.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum ResponseMessages {
    /**
     * member
     */
    MEMBER_NOT_FOUND("MEMBER-001", "존재하지 않는 유저입니다."),
    UPDATE_PHONE_NUMBER_SUCCESS("MEMBER-002", "휴대폰 번호 업데이트를 성공헀습니다"),

    /**
     * auth
     */
    ACCESS_TOKEN_NOT_FOUND("AUTH-001", "헤더에 토큰이 존재하지 않습니다."),
    AUTH_BAD_REQUEST("AUTH-002", "올바르지 않은 접근입니다"),
    LOGIN_SUCCESS("AUTH-003", "로그인을 성공했습니다"),
    LOGIN_FAIL("AUTH-004", "로그인을 실패했습니다");

    // 도메인 추가해주세요
    private final String code;
    private final String message;
}
