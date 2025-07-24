package com.shinhan.pda_midterm_project.presentation.news.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockNewsResponseDto {
  private Long id;
  private String newsTitle;
  private String newsContent;
  private String newsUri;
  private String newsDate;
  private String newsImage;
  private LocalDateTime createdAt;
}