package com.shinhan.pda_midterm_project.domain.disclosure.dto;

import java.util.List;

public record DisclosureResponseDto(
        String ticker,
        Integer total_filings,
        List<DisclosureResult> results,
        Double total_cost_usd,
        String status
) {
}
