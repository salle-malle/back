package com.shinhan.pda_midterm_project.domain.disclosure.dto;

import java.time.LocalDate;

public record DisclosureSimpleDto(
        Long id,
        String disclosureTitle,
        String disclosureSummary,
        LocalDate disclosureDate,
        String stockName,
        String stockId) {
    public static DisclosureSimpleDto of(
            Long id,
            String disclosureTitle,
            String disclosureSummary,
            LocalDate disclosureDate,
            String stockName,
            String stockId) {
        return new DisclosureSimpleDto(id, disclosureTitle, disclosureSummary, disclosureDate, stockName, stockId);
    }
}
