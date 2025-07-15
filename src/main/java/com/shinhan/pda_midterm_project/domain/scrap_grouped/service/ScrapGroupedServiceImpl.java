package com.shinhan.pda_midterm_project.domain.scrap_grouped.service;

import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.repository.MemberStockSnapshotRepository;
import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import com.shinhan.pda_midterm_project.domain.scrap_group.repository.ScrapGroupRepository;
import com.shinhan.pda_midterm_project.domain.scrap_group.service.ScrapGroupService;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.model.ScrapGrouped;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.repository.ScrapGroupedRepository;
import com.shinhan.pda_midterm_project.presentation.scrapGroup.dto.ScrapGroupResponseDto;
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedDeleteRequestDto;
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
public class ScrapGroupedServiceImpl implements ScrapGroupedService { // ✅ 인터페이스 구현
    private final ScrapGroupedRepository scrapGroupedRepository; // ✅ ScrapGroupRepository 주입
    private final MemberStockSnapshotRepository memberStockSnapshotRepository;
    private final ScrapGroupRepository scrapGroupRepository;

    @Override
    public List<ScrapGroupedResponseDto> getScrapGrouped(Long memberId, Long scrapGroupId) {
        List<ScrapGrouped> scrapings = scrapGroupedRepository.findScrapsInGroup(memberId, scrapGroupId);
        return scrapings.stream()
                .map(ScrapGroupedResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ScrapGroupedResponseDto pushScrap(Long memberId, ScrapGroupedPushRequestDto requestDto) {
        // 1. 스크랩 그룹과 스냅샷 엔티티를 조회합니다.
        ScrapGroup scrapGroup = scrapGroupRepository.findById(requestDto.getScrapGroupId())
                .orElseThrow(() -> new EntityNotFoundException("해당 스크랩 그룹을 찾을 수 없습니다."));

        MemberStockSnapshot snapshot = memberStockSnapshotRepository.findById(requestDto.getMemberStockSnapshotId())
                .orElseThrow(() -> new EntityNotFoundException("해당 스냅샷을 찾을 수 없습니다."));

        // 2. [보안] 스크랩 그룹이 현재 로그인한 사용자의 소유인지 확인합니다.
        if (!scrapGroup.getMember().getId().equals(memberId)) {
            // 실제 애플리케이션에서는 권한 없음 예외(AccessDeniedException 등)를 발생시키는 것이 좋습니다.
            throw new SecurityException("자신의 스크랩 그룹에만 추가할 수 있습니다.");
        }

        // 3. [중복 방지] 이미 해당 그룹에 스냅샷이 추가되었는지 확인합니다.
        boolean alreadyExists = scrapGroupedRepository.existsByScrapGroupIdAndMemberStockSnapshotId(
                scrapGroup.getId(), snapshot.getId()
        );
        if (alreadyExists) {
            // 실제 애플리케이션에서는 이미 존재한다는 예외(e.g., DataIntegrityViolationException)를 발생시키는 것이 좋습니다.
            throw new IllegalStateException("이미 그룹에 추가된 스냅샷입니다.");
        }

        // 4. 새로운 ScrapGrouped 엔티티를 생성하고 저장합니다.
        ScrapGrouped newScrap = ScrapGrouped.builder()
                .scrapGroup(scrapGroup)
                .memberStockSnapshot(snapshot)
                .build();

        ScrapGrouped savedScrap = scrapGroupedRepository.save(newScrap);

        // 5. 저장된 엔티티를 DTO로 변환하여 반환합니다.
        return new ScrapGroupedResponseDto(savedScrap);
    }
    @Transactional
    @Override
    public ScrapGroupedResponseDto deleteScrap(Long memberId, Long scrapGroupedId) {
        // 1. ID를 사용하여 삭제할 ScrapGrouped 엔티티를 조회합니다.
        ScrapGrouped scrapToDelete = scrapGroupedRepository.findById(scrapGroupedId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 스크랩 항목을 찾을 수 없습니다."));

        // 2. [보안] 삭제 권한이 있는지 확인합니다. (로그인한 사용자가 해당 스크랩의 소유자인지)
        if (!scrapToDelete.getScrapGroup().getMember().getId().equals(memberId)) {
            throw new SecurityException("해당 스크랩을 삭제할 권한이 없습니다.");
        }

        // 3. 응답으로 반환할 DTO를 미리 생성합니다. (삭제 후에는 데이터를 읽을 수 없으므로)
        ScrapGroupedResponseDto responseDto = new ScrapGroupedResponseDto(scrapToDelete);

        // 4. 엔티티를 삭제합니다.
        scrapGroupedRepository.delete(scrapToDelete);

        // 5. 삭제된 항목의 정보가 담긴 DTO를 반환합니다.
        return responseDto;
    }
}