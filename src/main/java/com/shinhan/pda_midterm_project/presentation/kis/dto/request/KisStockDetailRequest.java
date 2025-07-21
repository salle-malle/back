package com.shinhan.pda_midterm_project.presentation.kis.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KisStockDetailRequest {
  @JsonProperty("stockCode")
  private String stockCode; // 주식 코드 (예: AAPL, TSLA, VOO)

  @JsonProperty("AUTH")
  private String AUTH; // 인증토큰

  @JsonProperty("EXCD")
  private String EXCD; // 거래소코드

  @JsonProperty("SYMB")
  private String SYMB; // 종목코드
}