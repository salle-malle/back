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
    UPDATE_PHONE_NUMBER_SUCCESS("MEMBER-002", "휴대폰 번호 업데이트를 성공헀습니다");

    // 도메인 추가해주세요
    private final String code;
    private final String message;
}
