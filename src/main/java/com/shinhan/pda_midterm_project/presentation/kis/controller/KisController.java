package com.shinhan.pda_midterm_project.presentation.kis.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.auth.service.KoreaInvestmentService;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.service.MemberService;
import com.shinhan.pda_midterm_project.domain.member_stock.service.MemberStockService;
import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisBalanceRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisStockDetailRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisBalanceResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisStockDetailResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisTokenResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.MemberStockResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/kis")
@RequiredArgsConstructor
public class KisController {

  private final KoreaInvestmentService koreaInvestmentService;
  private final MemberService memberService;
  private final MemberStockService memberStockService;

  /**
   * 한국투자증권 액세스 토큰 발급
   */
  @PostMapping("/token")
  public ResponseEntity<Response<KisTokenResponse>> getAccessToken(
      @RequestParam String appKey,
      @RequestParam String appSecret) {
    try {
      KisTokenResponse tokenResponse = koreaInvestmentService.getAccessToken(appKey, appSecret);
      return ResponseEntity.ok(Response.success(
          ResponseMessages.SUCCESS.getCode(),
          ResponseMessages.SUCCESS.getMessage(),
          tokenResponse));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Response.failure(
          ResponseMessages.API_ERROR.getCode(),
          "토큰 발급 실패: " + e.getMessage()));
    }
  }

  /**
   * 해외주식 현재가 상세 조회
   */
  @PostMapping("/stock-detail")
  @MemberOnly
  public ResponseEntity<Response<KisStockDetailResponse>> getStockDetail(@Auth Accessor accessor,
      @RequestBody KisStockDetailRequest request) {
    Long memberId = accessor.memberId();
    try {
      Member member = memberService.findById(memberId);
      String accessToken = member.getKisAccessToken();

      if (accessToken == null || accessToken.isEmpty()) {
        return ResponseEntity.badRequest().body(Response.failure(
            ResponseMessages.API_ERROR.getCode(),
            "액세스 토큰이 없습니다. 회원가입을 먼저 진행해주세요."));
      }

      // 기본값 설정
      if (request.getAUTH() == null) {
        request.setAUTH("");
      }
      if (request.getEXCD() == null) {
        request.setEXCD(request.getEXCD()); // 나스닥
      }
      if (request.getSYMB() == null) {
        request.setSYMB(request.getSYMB()); // 테슬라
      }

      KisStockDetailResponse response = koreaInvestmentService.getStockDetail(
          request, accessToken, member.getMemberAppKey(), member.getMemberAppSecret());

      return ResponseEntity.ok(Response.success(
          ResponseMessages.SUCCESS.getCode(),
          ResponseMessages.SUCCESS.getMessage(),
          response));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Response.failure(
          ResponseMessages.API_ERROR.getCode(),
          "주식 상세 조회 실패: " + e.getMessage()));
    }
  }

  /**
   * 해외주식 잔고 조회
   */
  @PostMapping("/balance/{memberId}")
  public ResponseEntity<Response<KisBalanceResponse>> getBalance(
      @PathVariable Long memberId,
      @RequestBody KisBalanceRequest request) {
    try {
      Member member = memberService.findById(memberId);
      String accessToken = member.getKisAccessToken();

      if (accessToken == null || accessToken.isEmpty()) {
        return ResponseEntity.badRequest().body(Response.failure(
            ResponseMessages.API_ERROR.getCode(),
            "액세스 토큰이 없습니다. 회원가입을 먼저 진행해주세요."));
      }

      // 기본값 설정
      if (request.getCANO() == null) {
        request.setCANO(member.getMemberAccountNumber());
      }
      if (request.getACNT_PRDT_CD() == null) {
        request.setACNT_PRDT_CD("01"); // 계좌상품코드
      }
      if (request.getOVRS_EXCG_CD() == null) {
        request.setOVRS_EXCG_CD("NASD"); // 나스닥
      }
      if (request.getTR_CRCY_CD() == null) {
        request.setTR_CRCY_CD("USD"); // 달러
      }

      KisBalanceResponse response = koreaInvestmentService.getBalance(
          request, accessToken, member.getMemberAppKey(), member.getMemberAppSecret());

      return ResponseEntity.ok(Response.success(
          ResponseMessages.SUCCESS.getCode(),
          ResponseMessages.SUCCESS.getMessage(),
          response));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Response.failure(
          ResponseMessages.API_ERROR.getCode(),
          "잔고 조회 실패: " + e.getMessage()));
    }
  }

  /**
   * 회원 주식 목록 조회
   */
  @GetMapping("/member-stocks")
  @MemberOnly
  public ResponseEntity<Response<List<MemberStockResponseDto>>> getMemberStocks(@Auth Accessor accessor) {
    try {
      Long memberId = accessor.memberId();

      Member member = memberService.findById(memberId);
      var memberStocks = memberStockService.getMemberStocks(member);

      List<MemberStockResponseDto> responseDtos = memberStocks.stream()
          .map(MemberStockResponseDto::new)
          .collect(Collectors.toList());

      return ResponseEntity.ok(Response.success(
          ResponseMessages.SUCCESS.getCode(),
          ResponseMessages.SUCCESS.getMessage(),
          responseDtos));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Response.failure(
          ResponseMessages.API_ERROR.getCode(),
          "회원 주식 조회 실패: " + e.getMessage()));
    }
  }

  /**
   * 회원 주식 정보 갱신 (KIS API에서 최신 데이터 가져오기)
   */
  @PostMapping("/refresh-member-stocks/{memberId}")
  public ResponseEntity<Response<String>> refreshMemberStocks(@PathVariable Long memberId) {
    try {
      Member member = memberService.findById(memberId);
      memberService.fetchAndSaveMemberStocks(member);

      return ResponseEntity.ok(Response.success(
          ResponseMessages.SUCCESS.getCode(),
          "회원 주식 정보가 성공적으로 갱신되었습니다."));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Response.failure(
          ResponseMessages.API_ERROR.getCode(),
          "주식 정보 갱신 실패: " + e.getMessage()));
    }
  }

  /**
   * 특정 주식 상세 정보 업데이트
   */
  @PostMapping("/update-stock-detail/{memberId}/{stockId}")
  public ResponseEntity<Response<String>> updateStockDetail(
      @PathVariable Long memberId,
      @PathVariable String stockId) {
    try {
      Member member = memberService.findById(memberId);
      String accessToken = member.getKisAccessToken();

      if (accessToken == null || accessToken.isEmpty()) {
        return ResponseEntity.badRequest().body(Response.failure(
            ResponseMessages.API_ERROR.getCode(),
            "액세스 토큰이 없습니다."));
      }

      memberStockService.updateStockDetailFromKis(stockId, accessToken, member);

      return ResponseEntity.ok(Response.success(
          ResponseMessages.SUCCESS.getCode(),
          "주식 상세 정보가 성공적으로 업데이트되었습니다."));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Response.failure(
          ResponseMessages.API_ERROR.getCode(),
          "주식 상세 정보 업데이트 실패: " + e.getMessage()));
    }
  }
}