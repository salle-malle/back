package com.shinhan.pda_midterm_project.presentation.scrap.dto;

import lombok.Getter;

@Getter
public class ScrapStatusResponse {
    private final boolean isScrapped;
    private Long scrapId;

    public ScrapStatusResponse(boolean isScrapped, Long scrapId) {
        this.isScrapped = isScrapped;
        this.scrapId = scrapId;
    }
}