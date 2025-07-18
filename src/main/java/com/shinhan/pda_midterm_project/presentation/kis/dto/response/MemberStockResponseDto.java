package com.shinhan.pda_midterm_project.presentation.kis.dto.response;

import com.shinhan.pda_midterm_project.domain.member_stock.model.MemberStock;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberStockResponseDto {
  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long memberId;
  private Stock stock;

  public MemberStockResponseDto(MemberStock memberStock) {
    this.id = memberStock.getId();
    this.createdAt = memberStock.getCreatedAt();
    this.updatedAt = memberStock.getUpdatedAt();
    this.memberId = memberStock.getMember().getId();
    this.stock = memberStock.getStock();
  }
}