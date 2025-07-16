package com.shinhan.pda_midterm_project.domain.disclosure.dto;

public record DisclosureRequestDto(String ticker, Integer days_back) {
    public static DisclosureRequestDto create(String ticker, Integer days_back) {
        return new DisclosureRequestDto(ticker, days_back);
    }
}
