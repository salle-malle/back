package com.shinhan.pda_midterm_project.domain.scrap_group.service;

import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import com.shinhan.pda_midterm_project.domain.scrap_group.repository.ScrapGroupRepository;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.repository.ScrapGroupedRepository;
import com.shinhan.pda_midterm_project.presentation.scrapGroup.dto.GroupInclusionStatusDto;
import com.shinhan.pda_midterm_project.presentation.scrapGroup.dto.ScrapGroupResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ScrapGroupServiceImpl implements ScrapGroupService {
    private final ScrapGroupRepository scrapGroupRepository;
    private final ScrapGroupedRepository scrapGroupedRepository; // 그룹 삭제 시 매핑 삭제를 위해 필요
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
    public ScrapGroupResponseDto createScrapGroup(Long memberId, String scrapGroupName) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버를 찾을 수 없습니다. id=" + memberId));
        ScrapGroup newScrapGroup = ScrapGroup.create(member, scrapGroupName);
        ScrapGroup savedScrapGroup = scrapGroupRepository.save(newScrapGroup);
        return new ScrapGroupResponseDto(savedScrapGroup);
    }

    @Override
    @Transactional
    public ScrapGroupResponseDto updateScrapGroup(Long memberId, Long scrapGroupId, String newScrapGroupName) {
        ScrapGroup scrapGroup = scrapGroupRepository.findById(scrapGroupId)
                .orElseThrow(() -> new EntityNotFoundException("해당 스크랩 그룹을 찾을 수 없습니다. id=" + scrapGroupId));

        // [보안] 그룹 소유주 확인
        if (!scrapGroup.getMember().getId().equals(memberId)) {
            throw new SecurityException("그룹 이름을 수정할 권한이 없습니다.");
        }

        scrapGroup.updateName(newScrapGroupName);
        return new ScrapGroupResponseDto(scrapGroup); // @Transactional에 의해 변경 감지되어 자동 저장됨
    }

    // --- [ ⚠️ 여기가 수정된 부분입니다 ] ---
    @Override
    @Transactional
    public ScrapGroupResponseDto deleteScrapGroup(Long memberId, Long scrapGroupId) {
        // 삭제할 스크랩 그룹을 찾습니다.
        ScrapGroup scrapGroupToDelete = scrapGroupRepository.findById(scrapGroupId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 스크랩 그룹을 찾을 수 없습니다."));

        // [보안] 삭제 권한이 있는지(그룹의 소유주인지) 확인합니다.
        if (!scrapGroupToDelete.getMember().getId().equals(memberId)) {
            throw new SecurityException("해당 스크랩 그룹을 삭제할 권한이 없습니다.");
        }

        // 2. (핵심) 엔티티를 삭제하기 전에, 반환할 DTO를 미리 생성합니다.
        ScrapGroupResponseDto responseDto = new ScrapGroupResponseDto(scrapGroupToDelete);

        // 이 그룹을 참조하고 있는 모든 스크랩 매핑(ScrapGrouped)을 먼저 삭제합니다.
        scrapGroupedRepository.bulkDeleteByScrapGroupId(scrapGroupId);

        // 모든 자식 관계가 삭제된 후, 스크랩 그룹 자체를 삭제합니다.
        scrapGroupRepository.delete(scrapGroupToDelete);

        // 3. 미리 만들어 둔 DTO를 반환합니다.
        return responseDto;
    }

    @Override
    @Transactional
    public List<GroupInclusionStatusDto> getGroupInclusionStatus(Long memberId, Long scrapId) {
        // 1. 사용자의 모든 그룹 목록을 가져옵니다.
        List<ScrapGroup> allUserGroups = scrapGroupRepository.findAllByMemberIdOrderByCreatedAtAsc(memberId);

        // 2. 특정 스크랩이 포함된 그룹들의 ID만 Set으로 가져옵니다. (빠른 조회를 위해)
        Set<Long> includedGroupIds = scrapGroupedRepository.findGroupIdsByScrapId(scrapId);

        // 3. 모든 그룹을 순회하며, 포함 여부를 체크하여 새로운 DTO 리스트를 생성합니다.
        return allUserGroups.stream()
                .map(group -> {
                    boolean isIncluded = includedGroupIds.contains(group.getId());
                    return new GroupInclusionStatusDto(group, isIncluded);
                })
                .collect(Collectors.toList());
    }
}