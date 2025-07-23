package com.shinhan.pda_midterm_project.domain.news.model;

import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "stock_id")
  private Stock stock;

  @Column(columnDefinition = "TEXT")
  private String newsTitle;

  @Column(columnDefinition = "TEXT")
  private String newsContent;

  @Column(columnDefinition = "TEXT")
  private String newsUri;

  @Column(columnDefinition = "TEXT")
  private String newsImage;

  @Column(columnDefinition = "TEXT")
  private String newsDate;
}