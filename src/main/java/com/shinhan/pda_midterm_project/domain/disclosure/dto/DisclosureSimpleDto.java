package com.shinhan.pda_midterm_project.domain.disclosure.dto;

import java.time.LocalDate;

public record DisclosureSimpleDto(String disclosureTitle, String disclosureSummary, LocalDate disclosureDate) {
    public static DisclosureSimpleDto of(String disclosureTitle, String disclosureSummary, LocalDate disclosureDate) {
        return new DisclosureSimpleDto(disclosureTitle, disclosureSummary, disclosureDate);
    }
}
