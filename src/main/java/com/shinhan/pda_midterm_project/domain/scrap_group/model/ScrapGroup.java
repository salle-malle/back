package com.shinhan.pda_midterm_project.domain.scrap_group.model;

import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ScrapGroup extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Column(length = 255)
  private String scrapGroupName;

  public static ScrapGroup create(Member member, String scrapGroupName){
    return ScrapGroup.builder()
            .member(member)
            .scrapGroupName(scrapGroupName)
            .build();
  }

  public void updateName(String newName) {
    this.scrapGroupName = newName;
  }
}


