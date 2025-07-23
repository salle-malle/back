package com.shinhan.pda_midterm_project.domain.earning_call.service;

import com.shinhan.pda_midterm_project.domain.earning_call.model.EarningCall;

import java.util.List;

public interface EarningCallService {

    /**
     * 모든 어닝콜 데이터 조회
     */
    List<EarningCall> getAllEarningCalls();

    /**
     * 특정 주식의 어닝콜 데이터 조회
     */
    List<EarningCall> getEarningCallsByStockId(String stockId);

    /**
     * 특정 날짜의 어닝콜 데이터 조회
     */
    List<EarningCall> getEarningCallsByDate(String date);

    /**
     * 사용자 보유종목의 어닝콜 데이터 조회
     */
    List<EarningCall> getEarningCallsByMemberId(Long memberId);

    /**
     * 사용자 보유 종목의 어닝콜 데이터 중, 다가올 어닝콜들 조회
     */
    List<EarningCall> getUpcomingEarningCall(Long memberId);

    /**
     * 회원의 보유주식에 대한 어닝콜 데이터를 CSV에서 파싱하여 생성
     */
    void createEarningCallsFromCsvForMemberStocks(Long memberId);
}