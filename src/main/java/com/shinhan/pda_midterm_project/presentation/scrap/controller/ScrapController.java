package com.shinhan.pda_midterm_project.presentation.scrap.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.scrap.service.ScrapService;
import com.shinhan.pda_midterm_project.presentation.scrap.dto.ScrapCreateRequestDto;
import com.shinhan.pda_midterm_project.presentation.scrap.dto.ScrapResponseDto;
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
}