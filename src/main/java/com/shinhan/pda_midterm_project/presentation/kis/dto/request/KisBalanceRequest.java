package com.shinhan.pda_midterm_project.presentation.kis.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KisBalanceRequest {
  @JsonProperty("CANO")
  private String CANO; // 종합계좌번호

  @JsonProperty("ACNT_PRDT_CD")
  private String ACNT_PRDT_CD; // 계좌상품코드

  @JsonProperty("OVRS_EXCG_CD")
  private String OVRS_EXCG_CD; // 해외거래소코드

  @JsonProperty("TR_CRCY_CD")
  private String TR_CRCY_CD; // 거래통화코드

  @JsonProperty("CTX_AREA_FK200")
  private String CTX_AREA_FK200; // 연속조회검색조건

  @JsonProperty("CTX_AREA_NK200")
  private String CTX_AREA_NK200; // 연속조회키
}