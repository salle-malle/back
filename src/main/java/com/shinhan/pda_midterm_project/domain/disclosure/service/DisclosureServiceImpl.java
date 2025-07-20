package com.shinhan.pda_midterm_project.domain.disclosure.service;

import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureResponseDto;
import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureSimpleDto;
import com.shinhan.pda_midterm_project.domain.disclosure.model.Disclosure;
import com.shinhan.pda_midterm_project.domain.disclosure.repository.DisclosureRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DisclosureServiceImpl implements DisclosureService {
    private final DisclosureRepository disclosureRepository;

    public List<DisclosureSimpleDto> getMyCurrentDisclosure(Long memberID) {
        Pageable pageable = PageRequest.of(0, 5);
        List<Disclosure> myCurrentDisclosure = disclosureRepository.getMyCurrentDisclosure(memberID, pageable);

        return myCurrentDisclosure.stream().map((disclosure)
                -> DisclosureSimpleDto.of(
                disclosure.getDisclosureTitle(),
                disclosure.getDisclosureSummary(),
                disclosure.getDisclosureDate())).toList();
    }
}
