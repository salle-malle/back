package com.shinhan.pda_midterm_project.presentation.scrapGrouped.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.service.ScrapGroupedService;
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedDeleteRequestDto;
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedPushRequestDto;
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shinhan.pda_midterm_project.common.response.ResponseMessages.*;

@RestController
@RequestMapping("/api/v1/scrapgrouped")
@RequiredArgsConstructor
public class ScrapGroupedController {
    private final ScrapGroupedService scrapGroupedService;

    // 특정 그룹에 포함된 스크랩 목록 조회
    @GetMapping("/{scrapGroupId}")//OK
    @MemberOnly
    public ResponseEntity<Response<List<ScrapGroupedResponseDto>>> getScrapsInGroup(@Auth Accessor accessor,
                                                                                    @PathVariable Long scrapGroupId) {
        Long memberId = accessor.memberId();
        List<ScrapGroupedResponseDto> scrapedItems = scrapGroupedService.getScrapsInGroup(memberId, scrapGroupId);
        return ResponseEntity.ok().body(Response.success(GET_SCRAP_GROUPED_SUCCESS.getCode(), GET_SCRAP_GROUPED_SUCCESS.getMessage(), scrapedItems));
    }

    // 특정 그룹에 스크랩 추가
    @PostMapping("/push")//OK
    @MemberOnly
    public ResponseEntity<Response<ScrapGroupedResponseDto>> addScrapToGroup(@Auth Accessor accessor,
                                                                             @RequestBody ScrapGroupedPushRequestDto requestDto) {
        Long memberId = accessor.memberId();
        ScrapGroupedResponseDto newMapping = scrapGroupedService.addScrapToGroup(memberId, requestDto);
        return ResponseEntity.ok()
                .body(Response.success(CREATE_SCRAP_GROUPED_SUCCESS.getCode(), CREATE_SCRAP_GROUPED_SUCCESS.getMessage(), newMapping));
    }

    // 특정 그룹에서 스크랩 제거
    @DeleteMapping("/delete")//OK
    @MemberOnly
    public ResponseEntity<Response<Void>> removeScrapFromGroup(@Auth Accessor accessor,
                                                               @RequestBody ScrapGroupedDeleteRequestDto requestDto) {
        Long memberId = accessor.memberId();
        scrapGroupedService.removeScrapFromGroup(memberId, requestDto.getScrapGroupedId());
        return ResponseEntity
                .ok().body(Response.success(DELETE_SCRAP_GROUPED_SUCCESS.getCode(), DELETE_SCRAP_GROUPED_SUCCESS.getMessage()));
    }
}