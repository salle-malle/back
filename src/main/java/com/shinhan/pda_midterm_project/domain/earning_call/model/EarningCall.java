package com.shinhan.pda_midterm_project.domain.earning_call.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EarningCall extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "stock_id")
  @JsonIgnore
  private Stock stock;

  @Column(length = 50)
  private String earningCallDate;
}