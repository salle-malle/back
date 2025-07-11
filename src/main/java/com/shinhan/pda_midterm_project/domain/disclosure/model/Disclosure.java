package com.shinhan.pda_midterm_project.domain.disclosure.model;

import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Disclosure extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "stock_id")
  private Stock stock;

  @Column(columnDefinition = "TEXT")
  private String disclosureTitle;

  @Column(columnDefinition = "TEXT")
  private String disclosureSummary;

  @Column(length = 255)
  private String disclosureDate;
}