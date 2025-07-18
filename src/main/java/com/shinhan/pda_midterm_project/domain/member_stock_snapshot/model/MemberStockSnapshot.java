package com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model;

import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import com.shinhan.pda_midterm_project.domain.investment_type_news_comment.model.InvestmentTypeNewsComment;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberStockSnapshot extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "investment_type_news_comment_id")
  private InvestmentTypeNewsComment investmentTypeNewsComment;
}