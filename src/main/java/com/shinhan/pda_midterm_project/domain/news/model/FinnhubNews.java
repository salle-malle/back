package com.shinhan.pda_midterm_project.domain.news.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

// API 응답에 있지만 우리에겐 필요 없는 필드는 무시하도록 설정
@RequiredArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinnhubNews {
    private String category;
    private long datetime;
    private String headline;
    private String image;
    private String related;
    private String source;
    private String summary;
    private String url;

    // Getter와 Setter, toString() 메서드를 추가해주세요.
    // (IDE의 자동 생성 기능 사용)
}
