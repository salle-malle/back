package com.shinhan.pda_midterm_project.domain.scrap_grouped.model;

import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ScrapGrouped extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long scrapGroupedId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_stock_snapshot_id")
  private MemberStockSnapshot memberStockSnapshot;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "scrap_group_id")
  private ScrapGroup scrapGroup;
}