package com.shinhan.pda_midterm_project.presentation.earningCall.dto;

import com.shinhan.pda_midterm_project.domain.earning_call.model.EarningCall;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EarningCallResponseDto {
  private Long id;
  private String stockId;
  private String stockName;
  private String earningCallDate;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static EarningCallResponseDto from(EarningCall earningCall) {
    Stock stock = earningCall.getStock();
    return EarningCallResponseDto.builder()
        .id(earningCall.getId())
        .stockId(stock != null ? stock.getStockId() : null)
        .stockName(stock != null ? stock.getStockName() : null)
        .earningCallDate(earningCall.getEarningCallDate())
        .createdAt(earningCall.getCreatedAt())
        .updatedAt(earningCall.getUpdatedAt())
        .build();
  }
}