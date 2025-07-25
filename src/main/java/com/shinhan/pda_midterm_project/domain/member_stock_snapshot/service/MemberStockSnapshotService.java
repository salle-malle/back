package com.shinhan.pda_midterm_project.domain.member_stock_snapshot.service;

import com.shinhan.pda_midterm_project.presentation.memberStockSnapshot.dto.MemberStockSnapshotDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberStockSnapshotService {
    MemberStockSnapshotDetailResponseDto getSnapshotDetail(Long memberId, Long snapshotId);
    Page<MemberStockSnapshotDetailResponseDto> getSnapshotList(Long memberId, Pageable pageable);
    public List<MemberStockSnapshotDetailResponseDto> getSnapshotsForLastWeek(Long memberId);
    List<MemberStockSnapshotDetailResponseDto> getSnapshotsByDate(Long memberId, java.sql.Date date);
    List<MemberStockSnapshotDetailResponseDto> getScrappedSnapshotsByStock(Long memberId, String stockCode);
}