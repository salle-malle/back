package com.shinhan.pda_midterm_project.domain.investment_type_news_comment.model;

import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import com.shinhan.pda_midterm_project.domain.summary.model.Summary;
import com.shinhan.pda_midterm_project.domain.investment_type.model.InvestmentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InvestmentTypeNewsComment extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "summary_id")
  private Summary summary;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "investment_id")
  private InvestmentType investmentType;

  @Column(columnDefinition = "TEXT")
  private String investmentTypeNewsContent;
}