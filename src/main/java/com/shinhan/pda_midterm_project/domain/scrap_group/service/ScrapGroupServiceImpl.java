package com.shinhan.pda_midterm_project.domain.scrap_group.service;

import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup; // ✅ import
import com.shinhan.pda_midterm_project.domain.scrap_group.repository.ScrapGroupRepository; // ✅ import
import com.shinhan.pda_midterm_project.domain.scrap_grouped.model.ScrapGrouped;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.repository.ScrapGroupedRepository;
import com.shinhan.pda_midterm_project.presentation.scrapGroup.dto.ScrapGroupResponseDto; // ✅ import
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // ✅ import
import java.util.stream.Collectors; // ✅ import

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ScrapGroupServiceImpl implements ScrapGroupService {
    private final ScrapGroupRepository scrapGroupRepository;
    private final ScrapGroupedRepository scrapGroupedRepository;
    private final MemberRepository memberRepository;
    @Override
    public List<ScrapGroupResponseDto> getScrapGroup(Long memberId) {
        List<ScrapGroup> scrapGroups = scrapGroupRepository.findAllByMemberIdOrderByCreatedAtAsc(memberId);

        return scrapGroups.stream()
                .map(ScrapGroupResponseDto::new)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public ScrapGroupResponseDto createScrapGroup(Long memberId, String scrapGroupName){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다. id=" + memberId));
        ScrapGroup newScrapGroup = ScrapGroup.create(member, scrapGroupName);
        ScrapGroup savedScrapGroup = scrapGroupRepository.save(newScrapGroup);

        return new ScrapGroupResponseDto(savedScrapGroup);
    }

    @Override
    @Transactional
    public ScrapGroupResponseDto updateScrapGroup(Long scrapGroupId, String newScrapGroupName) {
        ScrapGroup scrapGroup = scrapGroupRepository.findById(scrapGroupId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스크랩 그룹을 찾을 수 없습니다. id=" + scrapGroupId));
        scrapGroup.updateName(newScrapGroupName);

        return new ScrapGroupResponseDto(scrapGroup);
    }

    @Override
    @Transactional
    public ScrapGroupResponseDto deleteScrapGroup(Long memberId, Long scrapGroupId){
        ScrapGroup scrapToDelete = scrapGroupRepository.findById(scrapGroupId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 스크랩 항목을 찾을 수 없습니다."));

        if (!scrapToDelete.getMember().getId().equals(memberId)) {
            throw new SecurityException("해당 스크랩을 삭제할 권한이 없습니다.");
        }

        ScrapGroupResponseDto responseDto = new ScrapGroupResponseDto(scrapToDelete);
        scrapGroupedRepository.deleteAllByScrapGroupId(scrapGroupId);
        scrapGroupRepository.delete(scrapToDelete);

        return responseDto;
    }
}