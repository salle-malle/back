package com.shinhan.pda_midterm_project.domain.news.model;

import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class News extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 255)
  private String newsTitle;

  @Column(columnDefinition = "TEXT")
  private String newsContent;
}