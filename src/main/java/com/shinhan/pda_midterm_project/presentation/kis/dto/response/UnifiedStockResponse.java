package com.shinhan.pda_midterm_project.presentation.kis.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UnifiedStockResponse {

  @JsonProperty("stocks")
  public List<UnifiedStockItem> stocks; // 통합 주식 목록

  @JsonProperty("summary")
  public UnifiedStockSummary summary; // 통합 요약 정보

  @JsonProperty("rt_cd")
  public String rtCd; // 응답코드

  @JsonProperty("msg_cd")
  public String msgCd; // 메시지코드

  @JsonProperty("msg1")
  public String msg1; // 메시지

  @Getter
  @Setter
  @NoArgsConstructor
  public static class UnifiedStockItem {
    @JsonProperty("stock_type")
    public String stockType; // 주식 유형 (REGULAR: 일반주식, FRACTIONAL: 소수점주식)

    @JsonProperty("prdt_name")
    public String prdtName; // 상품명

    @JsonProperty("pdno")
    public String pdno; // 상품번호

    @JsonProperty("std_pdno")
    public String stdPdno; // 표준상품번호

    @JsonProperty("quantity")
    public String quantity; // 보유수량 (일반주식: ovrsCblcQty, 소수점주식: cblcQty13)

    @JsonProperty("avg_price")
    public String avgPrice; // 평균단가 (일반주식: pchsAvgPric, 소수점주식: avgUnpr3)

    @JsonProperty("current_price")
    public String currentPrice; // 현재가 (일반주식: nowPric2, 소수점주식: ovrsNowPric1)

    @JsonProperty("purchase_amount")
    public String purchaseAmount; // 매입금액 (일반주식: frcrPchsAmt1, 소수점주식: frcrPchsAmt)

    @JsonProperty("evaluation_amount")
    public String evaluationAmount; // 평가금액 (일반주식: ovrsStckEvluAmt, 소수점주식: frcrEvluAmt2)

    @JsonProperty("profit_loss_amount")
    public String profitLossAmount; // 손익금액 (일반주식: frcrEvluPflsAmt, 소수점주식: evluPflsAmt2)

    @JsonProperty("profit_loss_rate")
    public String profitLossRate; // 손익률 (일반주식: evluPflsRt, 소수점주식: evluPflsRt1)

    @JsonProperty("exchange")
    public String exchange; // 거래소 (일반주식: ovrsExcgCd, 소수점주식: ovrsExcgCd)

    @JsonProperty("currency")
    public String currency; // 통화 (일반주식: trCrcyCd, 소수점주식: buyCrcyCd)

    @JsonProperty("market_name")
    public String marketName; // 시장명 (소수점주식: trMketName)

    @JsonProperty("country")
    public String country; // 국가 (소수점주식: natnKorName)

    @JsonProperty("product_type")
    public String productType; // 상품유형 (소수점주식: prdtTypeCd)

    @JsonProperty("securities_division")
    public String securitiesDivision; // 증권구분 (소수점주식: sctsDvsnName)

    @JsonProperty("order_available_quantity")
    public String orderAvailableQuantity; // 주문가능수량 (소수점주식: ordPsblQty1)

    @JsonProperty("today_buy_quantity")
    public String todayBuyQuantity; // 당일매수수량 (소수점주식: thdtBuyCcldQty1)

    @JsonProperty("today_sell_quantity")
    public String todaySellQuantity; // 당일매도수량 (소수점주식: thdtSllCcldQty1)

    @JsonProperty("exchange_rate")
    public String exchangeRate; // 환율 (소수점주식: bassExrt)

    @JsonProperty("unit_amount")
    public String unitAmount; // 단위금액 (소수점주식: unitAmt)
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class UnifiedStockSummary {
    @JsonProperty("total_purchase_amount")
    public String totalPurchaseAmount; // 총 매입금액

    @JsonProperty("total_evaluation_amount")
    public String totalEvaluationAmount; // 총 평가금액

    @JsonProperty("total_profit_loss_amount")
    public String totalProfitLossAmount; // 총 손익금액

    @JsonProperty("total_profit_loss_rate")
    public String totalProfitLossRate; // 총 손익률

    @JsonProperty("total_assets")
    public String totalAssets; // 총 자산

    @JsonProperty("withdrawable_amount")
    public String withdrawableAmount; // 출금가능금액

    @JsonProperty("regular_stock_count")
    public Integer regularStockCount; // 일반주식 종목 수

    @JsonProperty("fractional_stock_count")
    public Integer fractionalStockCount; // 소수점주식 종목 수

    @JsonProperty("total_stock_count")
    public Integer totalStockCount; // 총 종목 수
  }
}