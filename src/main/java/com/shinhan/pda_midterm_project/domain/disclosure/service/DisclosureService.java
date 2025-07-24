package com.shinhan.pda_midterm_project.domain.disclosure.service;

import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureSimpleDto;
import java.util.List;

public interface DisclosureService {
    List<DisclosureSimpleDto> getMyCurrentDisclosure(Long memberId);

    DisclosureSimpleDto getMonoDisclosure(Long disclosureId);
}
