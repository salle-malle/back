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
    GET_SCRAP_GROUP_SUCCESS("GROUP-001", "유저 스크랩 그룹 가져오기 성공"),
    GET_SCRAP_GROUPED_SUCCESS("GROUPED-001", "유저 스크랩 그룹의 스크랩들 가져오기 성공"),
    POST_SCRAP_GROUPED_SUCCESS("GROUP-001", "유저 스크랩 그룹 추가 성공");
    // 도메인 추가해주세요
    private final String code;
    private final String message;
}