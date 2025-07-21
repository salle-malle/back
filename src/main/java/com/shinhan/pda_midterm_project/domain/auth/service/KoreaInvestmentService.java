package com.shinhan.pda_midterm_project.domain.auth.service;

import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisBalanceRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisPresentBalanceRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisStockDetailRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisBalanceResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisPresentBalanceResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisStockDetailResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisTokenResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.UnifiedStockResponse;

public interface KoreaInvestmentService {
    /**
     * 한국투자증권 액세스 토큰 발급
     */
    KisTokenResponse getAccessToken(String appKey, String appSecret);

    /**
     * 해외주식 현재가 상세 조회
     */
    KisStockDetailResponse getStockDetail(KisStockDetailRequest request, String accessToken, String appKey,
            String appSecret);

    /**
     * 해외주식 잔고 조회
     */
    KisBalanceResponse getBalance(KisBalanceRequest request, String accessToken, String appKey, String appSecret);

    /**
     * 해외주식 소수점 잔고 조회
     */
    KisPresentBalanceResponse getPresentBalance(KisPresentBalanceRequest request, String accessToken, String appKey,
            String appSecret);

    /**
     * 통합 주식 잔고 조회 (일반주식 + 소수점주식)
     */
    UnifiedStockResponse getUnifiedStocks(String accountNumber, String accessToken, String appKey, String appSecret);
}