package com.shinhan.pda_midterm_project.domain.disclosure.dto;

import com.shinhan.pda_midterm_project.domain.disclosure.model.EventType;
import java.util.List;

public record DisclosureResult(
    String title,
    List<String> summary,
    EventType event_type,
    String tone,
    String filing_date,
    String item_type_code,
    Integer importance_score,
    Integer keyword_density
) {
}
