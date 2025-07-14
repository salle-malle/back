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
    LOGIN_FAIL("AUTH-004", "로그인을 실패했습니다"),
    SIGNUP_SUCCESS("AUTH-005", "회원가입을 성공했습니다."),
    INVALID_CERTIFICATION_NUMBER("AUTH-006", "인증번호가 올바르지 않습니다."),
    CERTIFICATION_BAD_REQUEST("AUTH-007", "인증 번호 요청을 다시 수행하세요"),
    CERTIFICATION_REQUEST_SUCCESS("AUTH-008", "인증번호 요청을 성공했습니다."),
    CERTIFICATION_VERIFY_SUCCESS("AUTH-009", "인증번호 검증을 성공했습니다."),

    /**
     * scrap
     */
    GET_SCRAP_GROUP_SUCCESS("GROUP-001", "유저 스크랩 그룹 가져오기 성공"),
    GET_SCRAP_GROUPED_SUCCESS("GROUPED-001", "유저 스크랩 그룹의 스크랩들 가져오기 성공"),
    POST_SCRAP_GROUPED_SUCCESS("GROUP-001", "유저 스크랩 그룹 추가 성공");

    // 도메인 추가해주세요
    private final String code;
    private final String message;
}