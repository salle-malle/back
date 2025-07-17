package com.shinhan.pda_midterm_project.presentation.kis.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KisBalanceResponse {

  @JsonProperty("output1")
  private List<KisBalanceItem> output1; // 잔고내역

  @JsonProperty("output2")
  private KisBalanceSummary output2; // 잔고요약

  @Getter
  @NoArgsConstructor
  public static class KisBalanceItem {
    @JsonProperty("ovrs_item_name")
    private String ovrsItemName; // 해외종목명

    @JsonProperty("ovrs_pdno")
    private String ovrsPdno; // 해외상품번호

    @JsonProperty("ovrs_excg_cd")
    private String ovrsExcgCd; // 해외거래소코드

    @JsonProperty("tr_crcy_cd")
    private String trCrcyCd; // 거래통화코드

    @JsonProperty("ovrs_cblc_qty")
    private String ovrsCblcQty; // 해외잔고수량

    @JsonProperty("pchs_avg_pric")
    private String pchsAvgPric; // 매입평균가격

    @JsonProperty("frcr_pchs_amt1")
    private String frcrPchsAmt1; // 매입금액

    @JsonProperty("now_pric2")
    private String nowPric2; // 현재가

    @JsonProperty("ovrs_stck_evlu_amt")
    private String ovrsStckEvluAmt; // 해외주식평가금액

    @JsonProperty("frcr_evlu_pfls_amt")
    private String frcrEvluPflsAmt; // 해외평가손익금액

    @JsonProperty("evlu_pfls_rt")
    private String evluPflsRt; // 평가손익률
  }

  @Getter
  @NoArgsConstructor
  public static class KisBalanceSummary {
    @JsonProperty("frcr_pchs_amt1")
    private String frcrPchsAmt1; // 총매입금액

    @JsonProperty("tot_evlu_pfls_amt")
    private String totEvluPflsAmt; // 총평가손익금액

    @JsonProperty("ovrs_tot_pfls")
    private String ovrsTotPfls; // 해외총손익

    @JsonProperty("tot_pftrt")
    private String totPftrt; // 총수익률
  }
}