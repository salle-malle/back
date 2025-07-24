package com.shinhan.pda_midterm_project.domain.disclosure.service;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureSimpleDto;
import com.shinhan.pda_midterm_project.domain.disclosure.exception.DisclosureException;
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

    @Override
    public List<DisclosureSimpleDto> getMyCurrentDisclosure(Long memberID) {
        Pageable pageable = PageRequest.of(0, 5);
        List<Disclosure> myCurrentDisclosure = disclosureRepository.getMyCurrentDisclosure(memberID, pageable);

        return myCurrentDisclosure.stream().map((disclosure)
                        -> DisclosureSimpleDto.of(
                        disclosure.getId(),
                        disclosure.getDisclosureTitle(),
                        disclosure.getDisclosureSummary(),
                        disclosure.getDisclosureDate(),
                        disclosure.getStock().getStockId()))
                .toList();
    }

    @Override
    public DisclosureSimpleDto getMonoDisclosure(Long disclosureId) {
        Disclosure disclosure = findDisclosureById(disclosureId);

        return DisclosureSimpleDto.of(
                disclosure.getId(),
                disclosure.getDisclosureTitle(),
                disclosure.getDisclosureSummary(),
                disclosure.getDisclosureDate(),
                disclosure.getStock().getStockId()
        );
    }

    public Disclosure findDisclosureById(Long disclosureId) {
        return disclosureRepository.findById(disclosureId).orElseThrow(() ->
                new DisclosureException(ResponseMessages.GET_DISCLOSURE_DETAIL_FAIL)
        );
    }
}
