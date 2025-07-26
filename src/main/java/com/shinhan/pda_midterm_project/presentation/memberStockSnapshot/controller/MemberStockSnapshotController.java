package com.shinhan.pda_midterm_project.presentation.memberStockSnapshot.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.service.MemberStockSnapshotService;
import com.shinhan.pda_midterm_project.presentation.memberStockSnapshot.dto.MemberStockSnapshotDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shinhan.pda_midterm_project.common.response.ResponseMessages.GET_CARD_DETAIL_SUCCESS;
import static com.shinhan.pda_midterm_project.common.response.ResponseMessages.GET_CARD_LIST_SUCCESS;

@RestController
@RequestMapping("/api/v1/member-stock-snapshots") // API 경로 변경
@RequiredArgsConstructor
public class MemberStockSnapshotController {

    private final MemberStockSnapshotService memberStockSnapshotService;

    // 스냅샷 목록 조회 (페이지네이션)
    @GetMapping("/get-10-cards")
    @MemberOnly
    public ResponseEntity<Response<Page<MemberStockSnapshotDetailResponseDto>>> getSnapshotList(
            @Auth Accessor accessor,
            @PageableDefault(size = 10, sort = "createdAt,desc") Pageable pageable) {

        Page<MemberStockSnapshotDetailResponseDto> snapshotPage = memberStockSnapshotService.getSnapshotList(accessor.memberId(), pageable);
        return ResponseEntity.ok().body(Response.success(GET_CARD_LIST_SUCCESS.getCode(), GET_CARD_LIST_SUCCESS.getMessage(),  snapshotPage));
    }

    @GetMapping("")
    @MemberOnly
    public ResponseEntity<Response<List<MemberStockSnapshotDetailResponseDto>>> getSnapshotListForLastWeek(
            @Auth Accessor accessor) {

        List<MemberStockSnapshotDetailResponseDto> snapshots = memberStockSnapshotService.getSnapshotsForLastWeek(accessor.memberId());

        // 성공 응답의 제네릭 타입을 List로 변경합니다.
        return ResponseEntity.ok().body(Response.success(GET_CARD_LIST_SUCCESS.getCode(), GET_CARD_LIST_SUCCESS.getMessage(), snapshots));
    }

    // 스냅샷 상세 정보 조회
    @GetMapping("/{snapshotId}")
    @MemberOnly
    public ResponseEntity<Response<MemberStockSnapshotDetailResponseDto>> getSnapshotDetail(
            @Auth Accessor accessor,
            @PathVariable Long snapshotId) {

        MemberStockSnapshotDetailResponseDto snapshotDetail = memberStockSnapshotService.getSnapshotDetail(accessor.memberId(), snapshotId);
        return ResponseEntity.ok().body(Response.success(GET_CARD_DETAIL_SUCCESS.getCode(), GET_CARD_DETAIL_SUCCESS.getMessage(), snapshotDetail));
    }

    @GetMapping("/by-date")
    @MemberOnly
    public ResponseEntity<Response<List<MemberStockSnapshotDetailResponseDto>>> getSnapshotsByDate(
            @Auth Accessor accessor,
            @RequestParam("date") String date // yyyy-MM-dd
    ) {
        java.sql.Date sqlDate = java.sql.Date.valueOf(date);
        List<MemberStockSnapshotDetailResponseDto> snapshots = memberStockSnapshotService.getSnapshotsByDate(accessor.memberId(), sqlDate);
        return ResponseEntity.ok().body(Response.success("GET_CARD_BY_DATE", "해당 날짜의 카드 목록 조회 성공", snapshots));
    }

    //종목별 스크랩 조회
    @GetMapping("/scraps")
    @MemberOnly
    public ResponseEntity<Response<List<MemberStockSnapshotDetailResponseDto>>> getScrappedSnapshotsByStock(
            @Auth Accessor accessor,
            @RequestParam("stockCode") String stockCode
    ) {
        List<MemberStockSnapshotDetailResponseDto> snapshots = memberStockSnapshotService.getScrappedSnapshotsByStock(accessor.memberId(), stockCode);
        return ResponseEntity.ok().body(Response.success("GET_SCRAPPED_CARD_BY_STOCK", "종목별 스크랩 조회", snapshots));
    }
}