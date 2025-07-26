package com.shinhan.pda_midterm_project.domain.disclosure.service;

import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureSimpleDto;
import java.time.LocalDate;
import java.util.List;

public interface DisclosureService {
    List<DisclosureSimpleDto> getMyCurrentDisclosure(Long memberId);

    DisclosureSimpleDto getMonoDisclosure(Long disclosureId);

    List<DisclosureSimpleDto> getMyDisclosures(Long memberId);
}
