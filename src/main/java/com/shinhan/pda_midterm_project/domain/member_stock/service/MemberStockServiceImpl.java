package com.shinhan.pda_midterm_project.domain.member_stock.service;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.service.KoreaInvestmentService;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member_stock.exception.MemberStockException;
import com.shinhan.pda_midterm_project.domain.member_stock.model.MemberStock;
import com.shinhan.pda_midterm_project.domain.member_stock.repository.MemberStockRepository;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.domain.stock.repository.StockRepository;
import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisStockDetailRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisStockDetailResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberStockServiceImpl implements MemberStockService {

  private final MemberStockRepository memberStockRepository;
  private final StockRepository stockRepository;
  private final KoreaInvestmentService koreaInvestmentService;

  @Override
  public void updateStockDetailFromKis(String stockId, String accessToken, Member member) {
    // Stock 정보 조회하여 거래소 확인
    Optional<Stock> stock = stockRepository.findById(stockId);
    if (stock.isPresent()) {
      String exchangeCode = stock.get().getOvrsExcgCd();

      // 거래소별로 상세정보 업데이트
      updateStockDetailFromKisWithExchange(stockId, accessToken, member, exchangeCode);
    }
  }

  private void updateStockWithDetailInfo(String stockId, KisStockDetailResponse.KisStockDetailOutput output) {
    Optional<Stock> existingStock = stockRepository.findById(stockId);

    if (existingStock.isPresent()) {
      Stock stock = existingStock.get();

      // 상세 정보로 Stock 업데이트 (새로운 필드명 사용)
      Stock updatedStock = Stock.builder()
          .stockId(stock.getStockId())
          .stockName(stock.getStockName())
          // 기본 주식 정보
          .rsym(output.getRsym())
          .zdiv(output.getZdiv())
          .curr(output.getCurr())
          .vnit(output.getVnit())
          // 가격 정보
          .open(parseBigDecimal(output.getOpen()))
          .high(parseBigDecimal(output.getHigh()))
          .low(parseBigDecimal(output.getLow()))
          .last(parseBigDecimal(output.getLast()))
          .base(parseBigDecimal(output.getBase()))
          // 거래량/거래대금
          .pvol(parseLong(output.getPvol()))
          .pamt(parseLong(output.getPamt()))
          .tvol(parseLong(output.getTvol()))
          .tamt(parseLong(output.getTamt()))
          // 가격 제한
          .uplp(parseBigDecimal(output.getUplp()))
          .dnlp(parseBigDecimal(output.getDnlp()))
          // 52주 정보
          .h52p(parseBigDecimal(output.getH52p()))
          .h52d(output.getH52d())
          .l52p(parseBigDecimal(output.getL52p()))
          .l52d(output.getL52d())
          // 재무 지표
          .perx(parseBigDecimal(output.getPerx()))
          .pbrx(parseBigDecimal(output.getPbrx()))
          .epsx(parseBigDecimal(output.getEpsx()))
          .bpsx(parseBigDecimal(output.getBpsx()))
          // 시장 정보
          .shar(parseLong(output.getShar()))
          .mcap(parseLong(output.getMcap()))
          .tomv(parseLong(output.getTomv()))
          // 현재가 등락 정보
          .tXprc(parseBigDecimal(output.getTXprc()))
          .tXdif(parseBigDecimal(output.getTXdif()))
          .tXrat(parseBigDecimal(output.getTXrat()))
          .tRate(parseBigDecimal(output.getTRate()))
          .tXsgn(output.getTXsgn())
          // 전일 등락 정보
          .pXprc(parseBigDecimal(output.getPXprc()))
          .pXdif(parseBigDecimal(output.getPXdif()))
          .pXrat(parseBigDecimal(output.getPXrat()))
          .pRate(parseBigDecimal(output.getPRate()))
          .pXsng(output.getPXsng())
          // 거래 정보
          .eOrdyn(output.getEOrdyn())
          .eHogau(parseBigDecimal(output.getEHogau()))
          .eIcod(output.getEIcod())
          .eParp(parseBigDecimal(output.getEParp()))
          .etypNm(output.getEtypNm())
          // 기존 필드들
          .stockImageUri(stock.getStockImageUri())
          .stockIsDelisted(false)
          .ovrsPdno(stock.getOvrsPdno())
          .ovrsItemName(stock.getOvrsItemName())
          .ovrsExcgCd(stock.getOvrsExcgCd())
          .trCrcyCd(output.getCurr())
          .prdtTypeCd(stock.getPrdtTypeCd())
          .build();

      stockRepository.save(updatedStock);
    }
  }

  private BigDecimal parseBigDecimal(String value) {
    if (value == null || value.trim().isEmpty()) {
      return BigDecimal.ZERO;
    }
    try {
      return new BigDecimal(value.trim());
    } catch (NumberFormatException e) {
      return BigDecimal.ZERO;
    }
  }

  private Long parseLong(String value) {
    if (value == null || value.trim().isEmpty()) {
      return 0L;
    }
    try {
      return Long.parseLong(value.trim());
    } catch (NumberFormatException e) {
      return 0L;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<MemberStock> getMemberStocks(Member member) {
    return memberStockRepository.findByMemberWithStock(member);
  }

  @Override
  @Transactional(readOnly = true)
  public MemberStock getMemberStock(Member member, String stockId) {
    return memberStockRepository.findByMemberAndStock_StockId(member, stockId)
        .orElseThrow(() -> new MemberStockException(ResponseMessages.MEMBER_NO_STOCKS));
  }
  /**
   * 회원이 보유한 모든 주식의 상세정보를 갱신
   */
  @Override
  @Transactional
  public void refreshAllMemberStockDetails(Member member) {
    String accessToken = member.getKisAccessToken();
    if (accessToken == null || accessToken.isEmpty()) {
      return;
    }

    // 회원이 보유한 모든 주식 조회
    List<MemberStock> memberStocks = getMemberStocks(member);

    for (MemberStock memberStock : memberStocks) {
      try {
        String stockId = memberStock.getStock().getStockId();
        String exchangeCode = memberStock.getStock().getOvrsExcgCd();

        // 거래소별로 다른 API 파라미터 사용
        updateStockDetailFromKisWithExchange(stockId, accessToken, member, exchangeCode);

        // API 호출 간격 조절 (너무 빠른 요청 방지)
        Thread.sleep(100);

      } catch (Exception e) {
        // 에러 처리
      }
    }
  }

  /**
   * 거래소별로 주식 상세정보 업데이트
   */
  private void updateStockDetailFromKisWithExchange(String stockId, String accessToken, Member member,
      String exchangeCode) {
    // 거래소 코드 매핑
    String apiExchangeCode = mapExchangeCode(exchangeCode);

    // 주식 상세 정보 조회
    KisStockDetailRequest request = new KisStockDetailRequest();
    // AUTH 파라미터 제거 - 헤더의 authorization만 사용
    request.setEXCD(apiExchangeCode);
    request.setSYMB(stockId);

    try {
      KisStockDetailResponse response = koreaInvestmentService.getStockDetail(request, accessToken,
          member.getMemberAppKey(), member.getMemberAppSecret());

      if (response != null && response.getOutput() != null) {
        updateStockWithDetailInfo(stockId, response.getOutput());
      }
    } catch (Exception e) {
      // 에러 처리
    }
  }

  /**
   * 거래소 코드 매핑
   */
  private String mapExchangeCode(String exchangeCode) {
    if (exchangeCode == null) {
      return "NAS"; // 기본값
    }

    switch (exchangeCode.toUpperCase()) {
      case "NASD":
      case "NASDAQ":
        return "NAS";
      case "NYSE":
        return "NYS";
      case "AMEX":
      case "AMX":
      case "AMS":
        return "AMS";
      default:
        return "NAS";
    }
  }
}
