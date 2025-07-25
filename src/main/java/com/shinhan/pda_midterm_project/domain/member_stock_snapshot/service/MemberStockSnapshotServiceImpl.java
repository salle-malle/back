package com.shinhan.pda_midterm_project.domain.member_stock_snapshot.service;

import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.repository.MemberStockSnapshotRepository;
import com.shinhan.pda_midterm_project.domain.scrap.repository.ScrapRepository;
import com.shinhan.pda_midterm_project.presentation.memberStockSnapshot.dto.MemberStockSnapshotDetailResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberStockSnapshotServiceImpl implements MemberStockSnapshotService {

    private final MemberStockSnapshotRepository memberStockSnapshotRepository;
    private final ScrapRepository scrapRepository;

    @Override
    public MemberStockSnapshotDetailResponseDto getSnapshotDetail(Long memberId, Long snapshotId) {
        MemberStockSnapshot snapshot = memberStockSnapshotRepository.findCardDetailById(snapshotId)
                .orElseThrow(() -> new EntityNotFoundException("Snapshot not found"));

        if (!snapshot.getMember().getId().equals(memberId)) {
            throw new SecurityException("You do not have permission to view this snapshot.");
        }

        snapshotId = snapshot.getId();
        Long scrappedSnapshotId = scrapRepository.findScrappedSnapshotIdByMemberIdAndSnapshotId(memberId, snapshotId);
        boolean isScrap = scrappedSnapshotId.equals(snapshot.getId());
        return new MemberStockSnapshotDetailResponseDto(snapshot, isScrap);
    }

@Override
public Page<MemberStockSnapshotDetailResponseDto> getSnapshotList(Long memberId, Pageable pageable) {
    // 1. 먼저 스냅샷 목록을 페이지네이션하여 가져옵니다.
    Page<MemberStockSnapshot> snapshotPage = memberStockSnapshotRepository.findCardsByMemberId(memberId, pageable);

    // 2. 현재 페이지에 있는 스냅샷들의 ID 목록을 추출합니다.
    List<Long> snapshotIdsOnPage = snapshotPage.getContent().stream()
            .map(MemberStockSnapshot::getId)
            .collect(Collectors.toList());

    // 3. 추출된 ID 목록을 사용해, 이 중에서 현재 사용자가 스크랩한 것들의 ID만 조회합니다. (단 한 번의 쿼리)
    Set<Long> scrappedSnapshotIds = scrapRepository.findScrappedSnapshotIdsByMemberIdAndSnapshotIds(memberId, snapshotIdsOnPage);

    // 4. 스냅샷을 DTO로 변환하면서, 스크랩 여부(scrappedSnapshotIds에 포함되는지)를 함께 전달합니다.
    return snapshotPage.map(snapshot -> {
        boolean isScrap = scrappedSnapshotIds.contains(snapshot.getId());
        return new MemberStockSnapshotDetailResponseDto(snapshot, isScrap);
    });
}
    public List<MemberStockSnapshotDetailResponseDto> getSnapshotsForLastWeek(Long memberId) {
        // 1. 조회 기간을 설정합니다. (오늘 포함 7일)
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(6);

        // LocalDate를 java.sql.Date 객체로 변환합니다.
        Date startDate = Date.valueOf(sevenDaysAgo);
        Date endDate = Date.valueOf(today);

        // 2. 리포지토리에서 기간 내의 스냅샷 목록을 가져옵니다.
        List<MemberStockSnapshot> snapshots = memberStockSnapshotRepository.findCardsByMemberIdAndCreatedAtBetween(memberId, startDate, endDate);

        // 3. 스냅샷 ID 목록을 추출합니다.
        List<Long> snapshotIds = snapshots.stream()
                .map(MemberStockSnapshot::getId)
                .collect(Collectors.toList());

        // 4. 스크랩된 ID Set을 조회합니다.
        Set<Long> scrappedSnapshotIds = scrapRepository.findScrappedSnapshotIdsByMemberIdAndSnapshotIds(memberId, snapshotIds);

        // 5. 스냅샷을 DTO로 변환하며 스크랩 여부를 설정합니다.
        return snapshots.stream()
                .map(snapshot -> {
                    boolean isScrap = scrappedSnapshotIds.contains(snapshot.getId());
                    return new MemberStockSnapshotDetailResponseDto(snapshot, isScrap);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MemberStockSnapshotDetailResponseDto> getSnapshotsByDate(Long memberId, java.sql.Date date) {
        List<MemberStockSnapshot> snapshots = memberStockSnapshotRepository.findCardsByMemberIdAndDate(memberId, date);

        List<Long> snapshotIds = snapshots.stream()
                .map(MemberStockSnapshot::getId)
                .collect(Collectors.toList());

        // 3. 추출된 ID 목록을 사용해, 이 중에서 현재 사용자가 스크랩한 것들의 ID만 조회합니다. (단 한 번의 쿼리)
        Set<Long> scrappedSnapshotIds = scrapRepository.findScrappedSnapshotIdsByMemberIdAndSnapshotIds(memberId, snapshotIds);

        return snapshots.stream().map(snapshot -> {
            // snapshot 객체가 아닌 snapshot의 ID로 포함 여부를 확인해야 합니다.
            boolean isScrap = scrappedSnapshotIds.contains(snapshot.getId());
            return new MemberStockSnapshotDetailResponseDto(snapshot, isScrap);
        }).toList();
    }

    public List<MemberStockSnapshotDetailResponseDto> getScrappedSnapshotsByStock(Long memberId, String stockCode) {
        List<MemberStockSnapshot> snapshots = memberStockSnapshotRepository.findScrappedCardsByMemberIdAndStockCode(memberId, stockCode);

        return snapshots.stream()
                .map(snapshot -> new MemberStockSnapshotDetailResponseDto(snapshot, true))
                .toList();
    }
}