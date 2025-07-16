package com.shinhan.pda_midterm_project.domain.auth.service;

import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisBalanceRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisStockDetailRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisBalanceResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisStockDetailResponse;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KoreaInvestmentServiceImpl implements KoreaInvestmentService {

  private final RestTemplate restTemplate = new RestTemplate();
  private static final String TOKEN_URL = "https://openapi.koreainvestment.com:9443/oauth2/tokenP";
  private static final String STOCK_DETAIL_URL = "https://openapi.koreainvestment.com:9443/uapi/overseas-price/v1/quotations/price-detail";
  private static final String BALANCE_URL = "https://openapi.koreainvestment.com:9443/uapi/overseas-stock/v1/trading/inquire-balance";

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
      UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(STOCK_DETAIL_URL)
          .queryParam("AUTH", request.getAUTH())
          .queryParam("EXCD", request.getEXCD())
          .queryParam("SYMB", request.getSYMB());

      String url = builder.toUriString();
      System.out.println("Requesting URL: " + url);
      System.out.println("Headers: " + headers);

      HttpEntity<String> httpRequest = new HttpEntity<>(headers);

      return restTemplate.exchange(url, HttpMethod.GET, httpRequest, KisStockDetailResponse.class).getBody();
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
      UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BALANCE_URL)
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
      // System.out.println("Requesting Balance URL: " + url);
      // System.out.println("Headers: " + headers);

      HttpEntity<String> httpRequest = new HttpEntity<>(headers);

      KisBalanceResponse response = restTemplate
          .exchange(url, HttpMethod.GET, httpRequest, KisBalanceResponse.class).getBody();

      // 응답 로깅 (데이터가 있을 때만 출력)
      if (response != null && response.getOutput1() != null && !response.getOutput1().isEmpty()) {
        System.out.println("Balance Response: " + response);
        System.out.println("Number of balance items: " + response.getOutput1().size());
        for (KisBalanceResponse.KisBalanceItem item : response.getOutput1()) {
          System.out.println("Stock: " + item.getOvrsItemName() + " (" + item.getOvrsPdno() + ")");
          System.out.println("  Exchange: " + item.getOvrsExcgCd());
          System.out.println("  Currency: " + item.getTrCrcyCd());
          System.out.println("  Quantity: " + item.getOvrsCblcQty());
          System.out.println("  Avg Price: " + item.getPchsAvgPric());
          System.out.println("  Purchase Amount: " + item.getFrcrPchsAmt1());
          System.out.println("  Current Price: " + item.getNowPric2());
          System.out.println("  Evaluation Amount: " + item.getOvrsStckEvluAmt());
          System.out.println("  Profit/Loss Amount: " + item.getFrcrEvluPflsAmt());
          System.out.println("  Profit/Loss Rate: " + item.getEvluPflsRt());
        }

        if (response.getOutput2() != null) {
          System.out.println("Summary:");
          System.out.println("  Total Purchase Amount: " + response.getOutput2().getFrcrPchsAmt1());
          System.out.println("  Total Evaluation Amount: " + response.getOutput2().getTotEvluPflsAmt());
          System.out.println("  Total Profit/Loss: " + response.getOutput2().getOvrsTotPfls());
          System.out.println("  Total Profit/Loss Rate: " + response.getOutput2().getTotPftrt());
        }
      } else {
        System.out.println("Balance Response: No data found for exchange");
      }

      return response;
    } catch (Exception e) {
      System.err.println("Error calling KIS Balance API: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }
}