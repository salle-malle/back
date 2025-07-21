package com.shinhan.pda_midterm_project.domain.scrap_grouped.model;

import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import com.shinhan.pda_midterm_project.domain.scrap.model.Scrap;
import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "UQ_SCRAP_GROUPED",
                columnNames = {"scrap_id", "scrap_group_id"}
        )//하나의 그룹에 같은 스크랩 여러개 x
})
public class ScrapGrouped extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "scrap_id")
  private Scrap scrap;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "scrap_group_id")
  private ScrapGroup scrapGroup;

  public static ScrapGrouped create(Scrap scrap, ScrapGroup scrapGroup) {
    return ScrapGrouped.builder()
            .scrap(scrap)
            .scrapGroup(scrapGroup)
            .build();
  }
}