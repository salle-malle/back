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
    UPDATE_NICKNAME_SUCCESS("MEMBER-003", "닉네임 변경에 성공했습니다."),
    MEMBER_ALREADY_EXISTS("MEMBER-004", "이미 존재하는 유저입니다"),
    INVALID_ACCOUNT_INFO("MEMBER-005", "올바르지 않은 계좌 정보입니다"),

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
     * card
     */
    GET_CARD_DETAIL_SUCCESS("CARD-001", "카드 상세 가져오기 성공"),
    GET_CARD_LIST_SUCCESS("CARD-002", "카드 리스트 가져오기 성공"),
    /**
     * scrap
     */
    GET_SCRAP_GROUP_SUCCESS("GROUP-001", "유저 스크랩 그룹 가져오기 성공"),
    GET_SCRAP_GROUPED_SUCCESS("GROUPED-001", "유저 스크랩 그룹의 스크랩들 가져오기 성공"),
    POST_SCRAP_GROUPED_SUCCESS("GROUP-002", "유저 스크랩 그룹 추가 성공"),
    CREATE_SCRAP_GROUPED_SUCCESS("GROUPED-002", "그룹의 스크랩 추가 성공"),
    DELETE_SCRAP_GROUPED_SUCCESS("GROUPED-003", "그룹의 스크랩 삭제 성공"),
    PUT_SCRAP_GROUP_NAME_SUCCESS("GROUP-003", "스크랩 그룹 이름 업데이트 성공"),
    DELETE_SCRAP_GROUP_SUCCESS("GROUP-004", "스크랩 그룹 삭제 성공"),
    CREATE_SCRAP_SUCCESS("SCRAP-001", "스크랩 추가 성공"),
    DELETE_SCRAP_SUCCESS("SCRAP-002", "스크랩 삭제 성공"),
    GET_SCRAP_GROUP_STATUS_SUCCESS("GROUP-004", "스크랩 여부 확인 성공"),
    GET_SCRAP_STATUS_SUCCESS("SCRAP-003", "스크랩 상태 확인 성공"),

    /**
     * earning call
     */
    EARNING_CALL_GET_ALL_SUCCESS("EARNING-001", "어닝콜 데이터 조회를 성공했습니다."),
    EARNING_CALL_GET_BY_STOCK_SUCCESS("EARNING-002", "특정 주식의 어닝콜 데이터 조회를 성공했습니다."),
    EARNING_CALL_GET_BY_DATE_SUCCESS("EARNING-003", "특정 날짜의 어닝콜 데이터 조회를 성공했습니다."),
    EARNING_CALL_GET_BY_MEMBER_SUCCESS("EARNING-004", "사용자 보유종목의 어닝콜 데이터 조회를 성공했습니다."),
    EARNING_CALL_GET_FAIL("EARNING-005", "어닝콜 데이터 조회 중 오류가 발생했습니다."),

    /**
     * stock
     */
    STOCK_CREATE_SUCCESS("STOCK-001", "주식 데이터가 성공적으로 생성되었습니다."),
    STOCK_CREATE_FAIL("STOCK-002", "주식 데이터 생성 중 오류가 발생했습니다."),
    STOCK_GET_ALL_SUCCESS("STOCK-003", "주식 데이터 조회를 성공했습니다."),
    STOCK_GET_ALL_FAIL("STOCK-004", "주식 데이터 조회 중 오류가 발생했습니다."),
    STOCK_NOT_FOUND("STOCK-005", "해당 주식 정보를 찾을 수 없습니다."),
    STOCK_ALREADY_EXISTS("STOCK-006", "이미 존재하는 주식 정보입니다."),

    /**
     * disclosure
     */
    GET_MY_CURRENT_DISCLOSURE_SUCCESS("DISCLOSURE-001", "나의 최근 공시정보를 성공적으로 조회했습니다."),
    GET_DISCLOSURE_DETAIL_FAIL("DISCLOSURE-002", "공시 정보 세부사항을 가져오는 것을 실패했습니다."),
    GET_DISCLOSURE_DETAIL_SUCCESS("DISCLOSURE-003", "공시 정보 세부사항을 가져오는 것을 성공했습니다."),
    GET_DISCLOSURES_SUCCESS("DISCLOSURE-004", "나의 전체 공시 정보를 가져오는 것을 성공했습니다."),

    /**
     * common
     */
    SUCCESS("SUCCESS-001", "요청이 성공적으로 처리되었습니다."),
    API_ERROR("API-001", "API 호출 중 오류가 발생했습니다."),

    /**
     * member stock
     */
    MEMBER_NO_STOCKS("MEMBER-STOCK-001", "사용자의 보유종목이 없습니다."),

    /**
     * main news
     */
    GET_CURRENT_MAIN_NEWS_SUCCESS("MAINNEWS-001", "최근 증시 뉴스 조회를 성공했습니다."),

    /**
     * notification
     */
    MARK_NOTIFICATION_AS_READ_SUCCESS("NOTI-001", "알림 읽음 처리에 성공했습니다."),
    HAS_UNREAD_NOTIFICATION_SUCCESS("NOTI-002", "읽지 않은 알림 존재 여부 조회 성공"),

    /*
     * my page
     */
    GET_MY_PAGE_SUCCESS("MYPAGE-001", "마이페이지 조회를 성공했습니다."),
    UPDATE_INVESTMENT_TYPE_SUCCESS("MYPAGE-004", "투자 성향이 성공적으로 변경되었습니다.");

    // 도메인 추가해주세요
    private final String code;
    private final String message;
}
