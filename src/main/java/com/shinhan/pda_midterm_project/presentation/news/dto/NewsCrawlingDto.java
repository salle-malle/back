package com.shinhan.pda_midterm_project.presentation.news.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

public class NewsCrawlingDto {
    // Python API에 보낼 요청 DTO
    @Getter
    public static class Request {
        private final List<String> tickers;

        public Request(List<String> tickers) {
            this.tickers = tickers;
        }
    }

    // Python API로부터 받을 응답 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Response {
        @JsonProperty("crawl_results")
        private Map<String, List<CrawledArticle>> crawlResults;
    }

    // 응답 DTO 내부에 있는 개별 기사 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class CrawledArticle {
        private String newsTitle;
        private String newsContent;
        private String newsUri;
        private String newsDate;
    }
}
