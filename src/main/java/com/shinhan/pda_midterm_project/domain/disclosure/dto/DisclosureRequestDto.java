package com.shinhan.pda_midterm_project.domain.disclosure.dto;

public record DisclosureRequestDto(String ticker, String start_date, String end_date) {
    public static DisclosureRequestDto create(String ticker, String start_date, String end_date) {
        return new DisclosureRequestDto(ticker, start_date, end_date);
    }
}
