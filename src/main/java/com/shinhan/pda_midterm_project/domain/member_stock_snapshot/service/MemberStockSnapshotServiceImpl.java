package com.shinhan.pda_midterm_project.domain.member_stock_snapshot.service;

import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.repository.MemberStockSnapshotRepository;
import com.shinhan.pda_midterm_project.presentation.memberStockSnapshot.dto.MemberStockSnapshotDetailResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberStockSnapshotServiceImpl implements MemberStockSnapshotService {

    private final MemberStockSnapshotRepository memberStockSnapshotRepository;

    @Override
    public MemberStockSnapshotDetailResponseDto getSnapshotDetail(Long memberId, Long snapshotId) {
        MemberStockSnapshot snapshot = memberStockSnapshotRepository.findCardDetailById(snapshotId)
                .orElseThrow(() -> new EntityNotFoundException("Snapshot not found"));

        if (!snapshot.getMember().getId().equals(memberId)) {
            throw new SecurityException("You do not have permission to view this snapshot.");
        }

        return new MemberStockSnapshotDetailResponseDto(snapshot);
    }

    @Override
    public Page<MemberStockSnapshotDetailResponseDto> getSnapshotList(Long memberId, Pageable pageable) {
        Page<MemberStockSnapshot> snapshotPage = memberStockSnapshotRepository.findCardsByMemberId(memberId, pageable);
        return snapshotPage.map(MemberStockSnapshotDetailResponseDto::new);
    }

    @Override
    public List<MemberStockSnapshotDetailResponseDto> getSnapshotsByDate(Long memberId, java.sql.Date date) {
        List<MemberStockSnapshot> snapshots = memberStockSnapshotRepository.findCardsByMemberIdAndDate(memberId, date);
        return snapshots.stream().map(MemberStockSnapshotDetailResponseDto::new).toList();
    }
}