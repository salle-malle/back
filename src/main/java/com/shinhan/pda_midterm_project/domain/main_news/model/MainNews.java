package com.shinhan.pda_midterm_project.domain.main_news.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MainNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String newsTitle;

    @Column(columnDefinition = "TEXT")
    private String newsContent;

    @Column(columnDefinition = "TEXT")
    private String newsUri;

    private LocalDateTime newsDate;

    public static MainNews create(String newsTitle, String newsContent, String newsUri, LocalDateTime newsDate) {
        return MainNews.builder()
                .newsTitle(newsTitle)
                .newsContent(newsContent)
                .newsUri(newsUri)
                .newsDate(newsDate)
                .build();
    }
}
