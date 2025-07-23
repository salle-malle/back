package com.shinhan.pda_midterm_project.presentation.kis.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KisPresentBalanceRequest {
  @JsonProperty("CANO")
  private String CANO; // 종합계좌번호

  @JsonProperty("ACNT_PRDT_CD")
  private String ACNT_PRDT_CD; // 계좌상품코드

  @JsonProperty("WCRC_FRCR_DVSN_CD")
  private String WCRC_FRCR_DVSN_CD; // 외화잔고구분코드

  @JsonProperty("NATN_CD")
  private String NATN_CD; // 국가코드

  @JsonProperty("TR_MKET_CD")
  private String TR_MKET_CD; // 거래시장코드

  @JsonProperty("INQR_DVSN_CD")
  private String INQR_DVSN_CD; // 조회구분코드
}