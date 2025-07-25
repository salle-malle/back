package com.shinhan.pda_midterm_project.presentation.kis.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.auth.service.KoreaInvestmentService;
import com.shinhan.pda_midterm_project.domain.earning_call.service.EarningCallService;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.service.MemberService;
import com.shinhan.pda_midterm_project.domain.member_stock.service.MemberStockService;
import com.shinhan.pda_midterm_project.domain.stock.repository.StockRepository;
import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisBalanceRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisPresentBalanceRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisStockDetailRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisBalanceResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisPresentBalanceResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisStockDetailResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisTokenResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.MemberStockResponseDto;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.UnifiedStockResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/kis")
@RequiredArgsConstructor
public class KisController {

	private final KoreaInvestmentService koreaInvestmentService;
	private final MemberService memberService;
	private final MemberStockService memberStockService;
	private final StockRepository stockRepository;
	private final EarningCallService earningCallService;

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
	public ResponseEntity<Response<KisStockDetailResponse>> getStockDetail(
			@Auth Accessor accessor,
			@RequestBody KisStockDetailRequest request) {
		try {
			Member member = memberService.findById(accessor.memberId());
			String accessToken = member.getKisAccessToken();

			if (accessToken == null || accessToken.isEmpty()) {
				return ResponseEntity.badRequest().body(Response.failure(
						ResponseMessages.API_ERROR.getCode(),
						"액세스 토큰이 없습니다. 회원가입을 먼저 진행해주세요."));
			}

			// 주식 코드가 없으면 에러 반환
			if (request.getStockCode() == null || request.getStockCode().isEmpty()) {
				return ResponseEntity.badRequest().body(Response.failure(
						ResponseMessages.API_ERROR.getCode(),
						"주식 코드가 필요합니다."));
			}

			// 주식 DB에서 거래소 정보 조회
			Optional<com.shinhan.pda_midterm_project.domain.stock.model.Stock> stockOpt = stockRepository
					.findById(request.getStockCode());

			if (stockOpt.isPresent()) {
				com.shinhan.pda_midterm_project.domain.stock.model.Stock stock = stockOpt.get();
				String exchangeCode = stock.getOvrsExcgCd();

				// 거래소 코드 매핑 (DB 코드를 KIS API 코드로 변환)
				String kisExchangeCode = mapExchangeCodeForKis(exchangeCode);

				// KIS API 요청용 객체 생성
				KisStockDetailRequest kisRequest = new KisStockDetailRequest();
				kisRequest.setAUTH(""); // AUTH는 항상 빈 문자열
				kisRequest.setEXCD(kisExchangeCode); // DB에서 조회한 거래소 코드
				kisRequest.setSYMB(request.getStockCode()); // 주식 코드

				KisStockDetailResponse response = koreaInvestmentService.getStockDetail(
						kisRequest, accessToken, member.getMemberAppKey(), member.getMemberAppSecret());

				// KIS API 응답에서 종목명이 빈 값이면 DB에서 조회한 값으로 대체
				if (response != null && response.getOutput() != null) {
					response.getOutput().setEtypNm(stock.getStockName());
				}

				return ResponseEntity.ok(Response.success(
						ResponseMessages.SUCCESS.getCode(),
						ResponseMessages.SUCCESS.getMessage(),
						response));
			} else {
				return ResponseEntity.badRequest().body(Response.failure(
						ResponseMessages.API_ERROR.getCode(),
						"해당 주식 정보를 찾을 수 없습니다: " + request.getStockCode()));
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Response.failure(
					ResponseMessages.API_ERROR.getCode(),
					"주식 상세 조회 실패: " + e.getMessage()));
		}
	}

	/**
	 * DB 거래소 코드를 KIS API 거래소 코드로 매핑
	 */
	private String mapExchangeCodeForKis(String dbExchangeCode) {
		if (dbExchangeCode == null) {
			return "NASD"; // 기본값
		}

		switch (dbExchangeCode.toUpperCase()) {
			case "NASD":
			case "NAS":
				return "NAS"; // 나스닥
			case "NYSE":
			case "NYS":
				return "NYS"; // 뉴욕
			case "AMEX":
			case "AMX":
				return "AMS"; // 아멕스
			default:
				return "NASD"; // 기본값
		}
	}

	/**
	 * 해외주식 잔고 조회
	 */
	@PostMapping("/balance")
	@MemberOnly
	public ResponseEntity<Response<KisBalanceResponse>> getBalance(
			@Auth Accessor accessor,
			@RequestBody KisBalanceRequest request) {
		try {
			Member member = memberService.findById(accessor.memberId());
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
	 * 해외주식 소수점 잔고 조회
	 */
	@PostMapping("/present-balance")
	@MemberOnly
	public ResponseEntity<Response<KisPresentBalanceResponse>> getPresentBalance(
			@Auth Accessor accessor,
			@RequestBody KisPresentBalanceRequest request) {
		try {
			Member member = memberService.findById(accessor.memberId());
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
			if (request.getWCRC_FRCR_DVSN_CD() == null) {
				request.setWCRC_FRCR_DVSN_CD("02"); // 외화잔고구분코드
			}
			if (request.getNATN_CD() == null) {
				request.setNATN_CD("000"); // 국가코드
			}
			if (request.getTR_MKET_CD() == null) {
				request.setTR_MKET_CD("00"); // 거래시장코드
			}
			if (request.getINQR_DVSN_CD() == null) {
				request.setINQR_DVSN_CD("00"); // 조회구분코드
			}

			KisPresentBalanceResponse response = koreaInvestmentService.getPresentBalance(
					request, accessToken, member.getMemberAppKey(), member.getMemberAppSecret());

			return ResponseEntity.ok(Response.success(
					ResponseMessages.SUCCESS.getCode(),
					ResponseMessages.SUCCESS.getMessage(),
					response));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Response.failure(
					ResponseMessages.API_ERROR.getCode(),
					"소수점 잔고 조회 실패: " + e.getMessage()));
		}
	}

	/**
	 * 회원 주식 목록 조회
	 */
	@GetMapping("/member-stocks")
	@MemberOnly
	public ResponseEntity<Response<List<MemberStockResponseDto>>> getMemberStocks(@Auth Accessor accessor) {
		try {
			Member member = memberService.findById(accessor.memberId());
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
	@PostMapping("/refresh-member-stocks")
	@MemberOnly
	public ResponseEntity<Response<String>> refreshMemberStocks(@Auth Accessor accessor) {
		try {
			Member member = memberService.findById(accessor.memberId());

			// 1. 주식 잔고 조회 및 저장
			memberService.fetchAndSaveMemberStocks(member);

			// 2. 모든 주식의 상세정보 갱신
			memberStockService.refreshAllMemberStockDetails(member);

			// 3. 회원의 보유주식에 대한 어닝콜 데이터 생성
			try {
				earningCallService.createEarningCallsFromCsvForMemberStocks(accessor.memberId());
				log.info("회원 ID: {} 의 보유주식에 대한 어닝콜 데이터 생성 완료", accessor.memberId());
			} catch (Exception e) {
				log.error("회원 ID: {} 의 보유주식 어닝콜 데이터 생성 중 오류 발생: {}", accessor.memberId(), e.getMessage());
				// 어닝콜 생성 실패는 전체 프로세스를 중단하지 않음
			}

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
	@PostMapping("/update-stock-detail/{stockId}")
	@MemberOnly
	public ResponseEntity<Response<String>> updateStockDetail(
			@Auth Accessor accessor,
			@PathVariable String stockId) {
		try {
			Member member = memberService.findById(accessor.memberId());
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

	/**
	 * 통합 주식 잔고 조회 (일반주식 + 소수점주식)
	 */
	@GetMapping("/unified-stocks")
	@MemberOnly
	public ResponseEntity<Response<UnifiedStockResponse>> getUnifiedStocks(@Auth Accessor accessor) {
		try {
			Member member = memberService.findById(accessor.memberId());
			String accessToken = member.getKisAccessToken();

			if (accessToken == null || accessToken.isEmpty()) {
				return ResponseEntity.badRequest().body(Response.failure(
						ResponseMessages.API_ERROR.getCode(),
						"액세스 토큰이 없습니다. 회원가입을 먼저 진행해주세요."));
			}

			UnifiedStockResponse response = koreaInvestmentService.getUnifiedStocks(
					member.getMemberAccountNumber(),
					accessToken,
					member.getMemberAppKey(),
					member.getMemberAppSecret());

			return ResponseEntity.ok(Response.success(
					ResponseMessages.SUCCESS.getCode(),
					ResponseMessages.SUCCESS.getMessage(),
					response));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Response.failure(
					ResponseMessages.API_ERROR.getCode(),
					"통합 주식 조회 실패: " + e.getMessage()));
		}
	}
}