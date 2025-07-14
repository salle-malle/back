package com.shinhan.pda_midterm_project.domain.scrap_grouped.service;

import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.repository.MemberStockSnapshotRepository;
import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import com.shinhan.pda_midterm_project.domain.scrap_group.repository.ScrapGroupRepository;
import com.shinhan.pda_midterm_project.domain.scrap_group.service.ScrapGroupService;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.model.ScrapGrouped;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.repository.ScrapGroupedRepository;
import com.shinhan.pda_midterm_project.presentation.scrapGroup.dto.ScrapGroupResponseDto;
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ScrapGroupedServiceImpl implements ScrapGroupedService { // ✅ 인터페이스 구현
    private final ScrapGroupedRepository scrapGroupedRepository; // ✅ ScrapGroupRepository 주입
    private final MemberStockSnapshotRepository memberStockSnapshotRepository;

    @Override
    public List<ScrapGroupedResponseDto> getScrapGrouped(Long memberId, Long scrapGroupId) {

        // 1. Repository의 새로운 메서드를 호출하여 요청된 로직에 맞는 데이터를 조회합니다.
        List<ScrapGrouped> scrapings = scrapGroupedRepository.findScrapsInGroup(memberId, scrapGroupId);

        // 2. 조회된 엔티티 목록을 DTO 목록으로 변환합니다.
        return scrapings.stream()
                .map(ScrapGroupedResponseDto::new)
                .collect(Collectors.toList());
    }
}

//@Override
//public List<ScrapGroupResponseDto> getScrapGroup(Long memberId) {
//    // 1. Repository를 통해 memberId에 해당하는 스크랩 그룹 목록을 조회
//    List<ScrapGroup> scrapGroups = scrapGroupRepository.findAllByMemberIdOrderByCreatedAtAsc(memberId);
//
//    // 2. 조회된 엔티티 목록을 DTO 목록으로 변환
//    return scrapGroups.stream()
//            .map(ScrapGroupResponseDto::new) // scrapGroup -> new ScrapGroupResponseDto(scrapGroup)
//            .collect(Collectors.toList());
//}