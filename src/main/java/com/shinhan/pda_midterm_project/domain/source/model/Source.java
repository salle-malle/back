package com.shinhan.pda_midterm_project.domain.source.model;

import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import com.shinhan.pda_midterm_project.domain.summary.model.Summary;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Source extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "summary_id")
  private Summary summary;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "stock_id")
  private Stock stock;

  @Column(columnDefinition = "TEXT")
  private String sourceUri;
}