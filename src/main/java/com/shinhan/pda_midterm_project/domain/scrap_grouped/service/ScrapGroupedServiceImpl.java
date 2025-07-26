// ScrapGroupedServiceImpl.java (Implementation) - 로직 전체 수정
package com.shinhan.pda_midterm_project.domain.scrap_grouped.service;

import com.shinhan.pda_midterm_project.domain.scrap.model.Scrap;
import com.shinhan.pda_midterm_project.domain.scrap.repository.ScrapRepository;
import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import com.shinhan.pda_midterm_project.domain.scrap_group.repository.ScrapGroupRepository;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.model.ScrapGrouped;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.repository.ScrapGroupedRepository;
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedDetailResponseDto;
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedPushRequestDto;
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ScrapGroupedServiceImpl implements ScrapGroupedService {
    private final ScrapGroupedRepository scrapGroupedRepository;
    private final ScrapRepository scrapRepository;
    private final ScrapGroupRepository scrapGroupRepository;

    @Override
    public List<ScrapGroupedResponseDto> getScrapsInGroup(Long memberId, Long scrapGroupId) {
        List<ScrapGrouped> scrapings = scrapGroupedRepository.findScrapsInGroup(memberId, scrapGroupId);
        return scrapings.stream()
                .map(ScrapGroupedResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScrapGroupedDetailResponseDto> getScrapsDetailInGroup(Long memberId, Long scrapGroupId) {
        // 1. JOIN FETCH가 적용된 쿼리를 호출하여 성능을 최적화합니다.
        List<ScrapGrouped> scrapings = scrapGroupedRepository.findScrapsInGroupWithDetails(memberId, scrapGroupId);

        // 2. 새로운 상세 DTO로 변환하여 반환합니다.
        return scrapings.stream()
                .map(ScrapGroupedDetailResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ScrapGroupedResponseDto addScrapToGroup(Long memberId, ScrapGroupedPushRequestDto requestDto) {
        Scrap scrap = scrapRepository.findById(requestDto.getScrapId())
                .orElseThrow(() -> new EntityNotFoundException("Scrap not found."));
        ScrapGroup scrapGroup = scrapGroupRepository.findById(requestDto.getScrapGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Scrap group not found."));

        if (!scrap.getMember().getId().equals(memberId) || !scrapGroup.getMember().getId().equals(memberId)) {
            throw new SecurityException("You do not have permission.");
        }

        boolean alreadyExists = scrapGroupedRepository.existsByScrapGroupIdAndScrapId(
                scrapGroup.getId(), scrap.getId()
        );
        if (alreadyExists) {
            throw new IllegalStateException("This scrap is already in the group.");
        }

        ScrapGrouped newMapping = ScrapGrouped.create(scrap, scrapGroup);
        ScrapGrouped savedMapping = scrapGroupedRepository.save(newMapping);
        return new ScrapGroupedResponseDto(savedMapping);
    }

    @Transactional
    @Override
    public void removeScrapFromGroup(Long memberId, Long scrapGroupedId) {
        ScrapGrouped mappingToDelete = scrapGroupedRepository.findById(scrapGroupedId)
                .orElseThrow(() -> new EntityNotFoundException("Scrap mapping not found."));

        if (!mappingToDelete.getScrapGroup().getMember().getId().equals(memberId)) {
            throw new SecurityException("You do not have permission to delete this.");
        }
        scrapGroupedRepository.delete(mappingToDelete);
    }
}