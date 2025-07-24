package com.shinhan.pda_midterm_project.presentation.scrap.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import com.shinhan.pda_midterm_project.domain.scrap.service.ScrapService;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.presentation.scrap.dto.ScrapCreateRequestDto;
import com.shinhan.pda_midterm_project.presentation.scrap.dto.ScrapResponseDto;
import com.shinhan.pda_midterm_project.presentation.scrap.dto.ScrapStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.shinhan.pda_midterm_project.common.response.ResponseMessages.*;

@RestController
@RequestMapping("/api/v1/scrap")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping("")//OK
    @MemberOnly
    public ResponseEntity<Response<ScrapResponseDto>> createScrap(@Auth Accessor accessor,
                                                                  @RequestBody ScrapCreateRequestDto requestDto) {
        Long memberId = accessor.memberId();
        ScrapResponseDto responseDto = scrapService.createScrap(memberId, requestDto.getMemberStockSnapshotId());
        return ResponseEntity.ok()
                .body(Response.success(
                        CREATE_SCRAP_SUCCESS.getCode(),
                        CREATE_SCRAP_SUCCESS.getMessage(),
                        responseDto
                ));
    }

    @DeleteMapping("/{scrapId}")//OK
    @MemberOnly
    public ResponseEntity<Response<Void>> deleteScrap(@Auth Accessor accessor,
                                                      @PathVariable Long scrapId) {
        Long memberId = accessor.memberId();
        scrapService.deleteScrap(memberId, scrapId);
        return ResponseEntity
                .ok()
                .body(Response.success(
                        DELETE_SCRAP_SUCCESS.getCode(),
                        DELETE_SCRAP_SUCCESS.getMessage()
                ));
    }

    @GetMapping("/status/{snapshotId}")
    @MemberOnly
    public ResponseEntity<Response<ScrapStatusResponse>> getScrapStatus(
            @Auth Accessor accessor,
            @PathVariable Long snapshotId) {

        ScrapStatusResponse responseDto = scrapService.getScrapStatus(accessor.memberId(), snapshotId);
        return ResponseEntity.ok().body(Response.success(GET_SCRAP_STATUS_SUCCESS.getCode(), GET_SCRAP_STATUS_SUCCESS.getMessage(), responseDto));
    }

    // --- [ ✨ 여기가 추가된 부분입니다 2 ] ---
    /**
     * 스냅샷 ID를 기준으로 스크랩 삭제 API
     * @param snapshotId 스크랩을 취소할 스냅샷의 ID
     */
    @DeleteMapping("/by-snapshot/{snapshotId}")
    @MemberOnly
    public ResponseEntity<Response<Void>> deleteScrapBySnapshotId(
            @Auth Accessor accessor,
            @PathVariable Long snapshotId) {

        scrapService.deleteScrapBySnapshotId(accessor.memberId(), snapshotId);
        return ResponseEntity.ok().body(Response.success(DELETE_SCRAP_SUCCESS.getCode(), DELETE_SCRAP_SUCCESS.getMessage()));
    }
}