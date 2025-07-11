package com.shinhan.pda_midterm_project.domain.stock.model;

import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Stock extends BaseEntity {
  @Id
  @Column(length = 255)
  private String stockId;

  @Column(nullable = false, length = 255)
  private String stockName;

  @Column
  private Integer stockClosingPrice;

  @Column
  private Long stockMarketCap;

  @Column(precision = 19, scale = 4)
  private BigDecimal stockWeek52High;

  @Column(precision = 19, scale = 4)
  private BigDecimal stockWeek52Low;

  @Column
  private Long stockAvgVolume;

  @Column(precision = 19, scale = 4)
  private BigDecimal stockPeRatio;

  @Column(precision = 19, scale = 4)
  private BigDecimal stockEps;

  @Column(precision = 19, scale = 4)
  private BigDecimal stockDividendYield;

  @Column(columnDefinition = "TEXT")
  private String stockImageUri;

  @Column
  private Boolean stockIsDelisted;
}