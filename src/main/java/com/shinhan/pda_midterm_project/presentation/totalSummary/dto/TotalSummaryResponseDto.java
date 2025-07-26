package com.shinhan.pda_midterm_project.presentation.totalSummary.dto;

public record TotalSummaryResponseDto(
        String totalSummary
) {
    public static TotalSummaryResponseDto of(String totalSummary) {
        return new TotalSummaryResponseDto(totalSummary);
    }
}
