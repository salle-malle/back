package com.shinhan.pda_midterm_project.domain.auth.service;

import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisBalanceRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisPresentBalanceRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisStockDetailRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisBalanceResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisPresentBalanceResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisStockDetailResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisTokenResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.UnifiedStockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KoreaInvestmentServiceImpl implements KoreaInvestmentService {

  private final RestTemplate restTemplate = new RestTemplate();
  private static final String TOKEN_URL = "https://openapi.koreainvestment.com:9443/oauth2/tokenP";
  private static final String STOCK_DETAIL_URL = "https://openapi.koreainvestment.com:9443/uapi/overseas-price/v1/quotations/price-detail";
  private static final String BALANCE_URL = "https://openapi.koreainvestment.com:9443/uapi/overseas-stock/v1/trading/inquire-balance";
  private static final String PRESENT_BALANCE_URL = "https://openapi.koreainvestment.com:9443/uapi/overseas-stock/v1/trading/inquire-present-balance";

  @Override
  public KisTokenResponse getAccessToken(String appKey, String appSecret) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, String> body = new HashMap<>();
    body.put("grant_type", "client_credentials");
    body.put("appkey", appKey);
    body.put("appsecret", appSecret);

    HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

    return restTemplate.postForObject(TOKEN_URL, request, KisTokenResponse.class);
  }

  @Override
  public KisStockDetailResponse getStockDetail(KisStockDetailRequest request, String accessToken, String appKey,
      String appSecret) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("authorization", "Bearer " + accessToken);
      headers.set("appkey", appKey);
      headers.set("appsecret", appSecret);
      headers.set("tr_id", "HHDFS76200200");
      headers.set("custtype", "P");

      // GET 요청으로 변경하고 파라미터를 쿼리스트링으로 전달
      UriComponentsBuilder builder = UriComponentsBuilder.newInstance().uri(URI.create(STOCK_DETAIL_URL))
          .queryParam("AUTH", request.getAUTH() != null ? request.getAUTH() : "")
          .queryParam("EXCD", request.getEXCD())
          .queryParam("SYMB", request.getSYMB());

      String url = builder.toUriString();

      HttpEntity<String> httpRequest = new HttpEntity<>(headers);

      KisStockDetailResponse response = restTemplate
          .exchange(url, HttpMethod.GET, httpRequest, KisStockDetailResponse.class).getBody();

      return response;
    } catch (Exception e) {
      System.err.println("Error calling KIS API: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public KisBalanceResponse getBalance(KisBalanceRequest request, String accessToken, String appKey, String appSecret) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("authorization", "Bearer " + accessToken);
      headers.set("appkey", appKey);
      headers.set("appsecret", appSecret);
      headers.set("tr_id", "TTTS3012R"); // 해외주식 잔고 조회
      headers.set("custtype", "P");

      // GET 요청으로 변경하고 파라미터를 쿼리스트링으로 전달
      UriComponentsBuilder builder = UriComponentsBuilder.newInstance().uri(URI.create(BALANCE_URL))
          .queryParam("CANO", request.getCANO())
          .queryParam("ACNT_PRDT_CD", request.getACNT_PRDT_CD())
          .queryParam("OVRS_EXCG_CD", request.getOVRS_EXCG_CD())
          .queryParam("TR_CRCY_CD", request.getTR_CRCY_CD());

      // CTX_AREA_FK200과 CTX_AREA_NK200은 항상 포함 (값이 null이어도 빈 값으로)
      builder.queryParam("CTX_AREA_FK200",
          request.getCTX_AREA_FK200() != null ? request.getCTX_AREA_FK200() : "");
      builder.queryParam("CTX_AREA_NK200",
          request.getCTX_AREA_NK200() != null ? request.getCTX_AREA_NK200() : "");

      String url = builder.toUriString();

      HttpEntity<String> httpRequest = new HttpEntity<>(headers);

      KisBalanceResponse response = restTemplate
          .exchange(url, HttpMethod.GET, httpRequest, KisBalanceResponse.class).getBody();

      return response;
    } catch (Exception e) {
      System.err.println("Error calling KIS Balance API: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public KisPresentBalanceResponse getPresentBalance(KisPresentBalanceRequest request, String accessToken,
      String appKey, String appSecret) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("authorization", "Bearer " + accessToken);
      headers.set("appkey", appKey);
      headers.set("appsecret", appSecret);
      headers.set("tr_id", "CTRP6504R"); // 해외주식 소수점 잔고 조회

      // GET 요청으로 변경하고 파라미터를 쿼리스트링으로 전달
      UriComponentsBuilder builder = UriComponentsBuilder.newInstance().uri(URI.create(PRESENT_BALANCE_URL))
          .queryParam("CANO", request.getCANO())
          .queryParam("ACNT_PRDT_CD", request.getACNT_PRDT_CD())
          .queryParam("WCRC_FRCR_DVSN_CD", request.getWCRC_FRCR_DVSN_CD())
          .queryParam("NATN_CD", request.getNATN_CD())
          .queryParam("TR_MKET_CD", request.getTR_MKET_CD())
          .queryParam("INQR_DVSN_CD", request.getINQR_DVSN_CD());

      String url = builder.toUriString();

      HttpEntity<String> httpRequest = new HttpEntity<>(headers);

      KisPresentBalanceResponse response = restTemplate
          .exchange(url, HttpMethod.GET, httpRequest, KisPresentBalanceResponse.class).getBody();

      return response;
    } catch (Exception e) {
      System.err.println("Error calling KIS Present Balance API: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public UnifiedStockResponse getUnifiedStocks(String accountNumber, String accessToken, String appKey,
      String appSecret) {
    System.out.println("accountNumber");
    try {
      List<UnifiedStockResponse.UnifiedStockItem> allStocks = new ArrayList<>();

      // 1. 일반주식 조회
      List<UnifiedStockResponse.UnifiedStockItem> regularStocks = getRegularStocks(accountNumber, accessToken, appKey,
          appSecret);
      allStocks.addAll(regularStocks);

      // 2. 소수점주식 조회
      List<UnifiedStockResponse.UnifiedStockItem> fractionalStocks = getFractionalStocks(accountNumber, accessToken,
          appKey, appSecret);
      allStocks.addAll(fractionalStocks);

      // 3. 통합 요약 정보 생성
      UnifiedStockResponse.UnifiedStockSummary summary = createUnifiedSummary(allStocks);

      // 4. 응답 생성
      UnifiedStockResponse response = new UnifiedStockResponse();
      response.stocks = allStocks;
      response.summary = summary;
      response.rtCd = "0";
      response.msgCd = "SUCCESS";
      response.msg1 = "통합 주식 조회가 완료되었습니다.";

      return response;

    } catch (Exception e) {
      System.err.println("Error calling Unified Stocks API: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }

  private List<UnifiedStockResponse.UnifiedStockItem> getRegularStocks(String accountNumber, String accessToken,
      String appKey, String appSecret) {
    List<UnifiedStockResponse.UnifiedStockItem> regularStocks = new ArrayList<>();

    try {
      // 나스닥 일반주식 조회
      KisBalanceRequest request = new KisBalanceRequest();
      request.setCANO(accountNumber);
      request.setACNT_PRDT_CD("01");
      request.setOVRS_EXCG_CD("NASD");
      request.setTR_CRCY_CD("USD");
      request.setCTX_AREA_FK200("");
      request.setCTX_AREA_NK200("");

      KisBalanceResponse response = getBalance(request, accessToken, appKey, appSecret);

      if (response.getOutput1() != null) {
        for (KisBalanceResponse.KisBalanceItem item : response.getOutput1()) {
          UnifiedStockResponse.UnifiedStockItem unifiedItem = new UnifiedStockResponse.UnifiedStockItem();
          unifiedItem.stockType = "REGULAR";
          unifiedItem.prdtName = item.getOvrsItemName();
          unifiedItem.pdno = item.getOvrsPdno();
          unifiedItem.quantity = item.getOvrsCblcQty();
          unifiedItem.avgPrice = item.getPchsAvgPric();
          unifiedItem.currentPrice = item.getNowPric2();
          unifiedItem.purchaseAmount = item.getFrcrPchsAmt1();
          unifiedItem.evaluationAmount = item.getOvrsStckEvluAmt();
          unifiedItem.profitLossAmount = item.getFrcrEvluPflsAmt();
          unifiedItem.profitLossRate = item.getEvluPflsRt();
          unifiedItem.exchange = item.getOvrsExcgCd();
          unifiedItem.currency = item.getTrCrcyCd();

          regularStocks.add(unifiedItem);
        }
      }
    } catch (Exception e) {
      System.err.println("Error fetching regular stocks: " + e.getMessage());
    }

    return regularStocks;
  }

  private List<UnifiedStockResponse.UnifiedStockItem> getFractionalStocks(String accountNumber, String accessToken,
      String appKey, String appSecret) {
    List<UnifiedStockResponse.UnifiedStockItem> fractionalStocks = new ArrayList<>();

    try {
      // 소수점주식 조회
      KisPresentBalanceRequest request = new KisPresentBalanceRequest();
      request.setCANO(accountNumber);
      request.setACNT_PRDT_CD("01");
      request.setWCRC_FRCR_DVSN_CD("02");
      request.setNATN_CD("000");
      request.setTR_MKET_CD("00");
      request.setINQR_DVSN_CD("00");

      KisPresentBalanceResponse response = getPresentBalance(request, accessToken, appKey, appSecret);

      if (response.getOutput1() != null) {
        for (KisPresentBalanceResponse.KisPresentBalanceItem item : response.getOutput1()) {
          UnifiedStockResponse.UnifiedStockItem unifiedItem = new UnifiedStockResponse.UnifiedStockItem();
          unifiedItem.stockType = "FRACTIONAL";
          unifiedItem.prdtName = item.getPrdtName();
          unifiedItem.pdno = item.getPdno();
          unifiedItem.stdPdno = item.getStdPdno();
          unifiedItem.quantity = item.getCblcQty13();
          unifiedItem.avgPrice = item.getAvgUnpr3();
          unifiedItem.currentPrice = item.getOvrsNowPric1();
          unifiedItem.purchaseAmount = item.getFrcrPchsAmt();
          unifiedItem.evaluationAmount = item.getFrcrEvluAmt2();
          unifiedItem.profitLossAmount = item.getEvluPflsAmt2();
          unifiedItem.profitLossRate = item.getEvluPflsRt1();
          unifiedItem.exchange = item.getOvrsExcgCd();
          unifiedItem.currency = item.getBuyCrcyCd();
          unifiedItem.marketName = item.getTrMketName();
          unifiedItem.country = item.getNatnKorName();
          unifiedItem.productType = item.getPrdtTypeCd();
          unifiedItem.securitiesDivision = item.getSctsDvsnName();
          unifiedItem.orderAvailableQuantity = item.getOrdPsblQty1();
          unifiedItem.todayBuyQuantity = item.getThdtBuyCcldQty1();
          unifiedItem.todaySellQuantity = item.getThdtSllCcldQty1();
          unifiedItem.exchangeRate = item.getBassExrt();
          unifiedItem.unitAmount = item.getUnitAmt();

          fractionalStocks.add(unifiedItem);
        }
      }
    } catch (Exception e) {
      System.err.println("Error fetching fractional stocks: " + e.getMessage());
    }

    return fractionalStocks;
  }

  private UnifiedStockResponse.UnifiedStockSummary createUnifiedSummary(
      List<UnifiedStockResponse.UnifiedStockItem> allStocks) {
    UnifiedStockResponse.UnifiedStockSummary summary = new UnifiedStockResponse.UnifiedStockSummary();

    BigDecimal totalPurchaseAmount = BigDecimal.ZERO;
    BigDecimal totalEvaluationAmount = BigDecimal.ZERO;
    BigDecimal totalProfitLossAmount = BigDecimal.ZERO;
    int regularStockCount = 0;
    int fractionalStockCount = 0;

    for (UnifiedStockResponse.UnifiedStockItem stock : allStocks) {
      try {
        if (stock.purchaseAmount != null && !stock.purchaseAmount.isEmpty()) {
          totalPurchaseAmount = totalPurchaseAmount.add(new BigDecimal(stock.purchaseAmount));
        }
        if (stock.evaluationAmount != null && !stock.evaluationAmount.isEmpty()) {
          totalEvaluationAmount = totalEvaluationAmount.add(new BigDecimal(stock.evaluationAmount));
        }
        if (stock.profitLossAmount != null && !stock.profitLossAmount.isEmpty()) {
          totalProfitLossAmount = totalProfitLossAmount.add(new BigDecimal(stock.profitLossAmount));
        }

        if ("REGULAR".equals(stock.stockType)) {
          regularStockCount++;
        } else if ("FRACTIONAL".equals(stock.stockType)) {
          fractionalStockCount++;
        }
      } catch (NumberFormatException e) {
        System.err.println("Error parsing number for stock: " + stock.pdno);
      }
    }

    summary.totalPurchaseAmount = totalPurchaseAmount.setScale(2, RoundingMode.HALF_UP).toString();
    summary.totalEvaluationAmount = totalEvaluationAmount.setScale(2, RoundingMode.HALF_UP).toString();
    summary.totalProfitLossAmount = totalProfitLossAmount.setScale(2, RoundingMode.HALF_UP).toString();

    // 총 손익률 계산
    if (totalPurchaseAmount.compareTo(BigDecimal.ZERO) > 0) {
      BigDecimal profitLossRate = totalProfitLossAmount.divide(totalPurchaseAmount, 4, RoundingMode.HALF_UP)
          .multiply(new BigDecimal("100"));
      summary.totalProfitLossRate = profitLossRate.toString();
    } else {
      summary.totalProfitLossRate = "0.00";
    }

    summary.totalAssets = totalEvaluationAmount.toString();
    summary.withdrawableAmount = totalEvaluationAmount.toString(); // 실제로는 출금가능금액 계산 필요
    summary.regularStockCount = regularStockCount;
    summary.fractionalStockCount = fractionalStockCount;
    summary.totalStockCount = regularStockCount + fractionalStockCount;

    return summary;
  }
}