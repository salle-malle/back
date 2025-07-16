package com.shinhan.pda_midterm_project.presentation.scrapGroup.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.scrap_group.service.ScrapGroupService;
import com.shinhan.pda_midterm_project.presentation.scrapGroup.dto.ScrapGroupDeleteRequestDto;
import com.shinhan.pda_midterm_project.presentation.scrapGroup.dto.ScrapGroupNameRequestDto;
import com.shinhan.pda_midterm_project.presentation.scrapGroup.dto.ScrapGroupResponseDto; // ✅ DTO import
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedPushRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shinhan.pda_midterm_project.common.response.ResponseMessages.*;

@RestController
@RequestMapping("/api/v1/scrapgroup")
@RequiredArgsConstructor
public class ScrapGroupController {
    private final ScrapGroupService scrapGroupService;

    @GetMapping("")
    @MemberOnly
    public ResponseEntity<Response<List<ScrapGroupResponseDto>>> getScrapGroup(@Auth Accessor accessor) { // ✅ 응답 타입을 DTO 리스트로 변경
        // 1. 사용자 ID를 2L로 고정
        Long memberId = accessor.memberId();

        // 2. Service를 호출하여 스크랩 그룹 목록 조회
        List<ScrapGroupResponseDto> scrapGroups = scrapGroupService.getScrapGroup(memberId);

        // 3. 성공 응답 반환
        return ResponseEntity
                .ok()
                .body(Response.success(
                        GET_SCRAP_GROUP_SUCCESS.getCode(),
                        GET_SCRAP_GROUP_SUCCESS.getMessage(),
                        scrapGroups
                ));
    }

    @PostMapping("/create")
    @MemberOnly
    public ResponseEntity<Response<ScrapGroupResponseDto>> createScrapGroup(@Auth Accessor accessor) { // ✅ 응답 타입을 DTO 리스트로 변경
        // 1. 사용자 ID를 2L로 고정
        Long memberId = accessor.memberId();
        String scrapGroupTitle = "title";

        // 2. Service를 호출하여 스크랩 그룹 목록 추가
        ScrapGroupResponseDto scrapGroups = scrapGroupService.createScrapGroup(memberId, scrapGroupTitle);

        // 3. 성공 응답 반환
        return ResponseEntity
                .ok()
                .body(Response.success(
                        POST_SCRAP_GROUPED_SUCCESS.getCode(),
                        POST_SCRAP_GROUPED_SUCCESS.getMessage(),
                        scrapGroups
                ));
    }

    @PutMapping("/groupnameupdate")
    public ResponseEntity<Response<ScrapGroupResponseDto>> updateScrapGroupName(
            @RequestBody ScrapGroupNameRequestDto requestDto)
     {
        ScrapGroupResponseDto scrapGroups = scrapGroupService.updateScrapGroup(requestDto.getScrapGroupId(), requestDto.getScrapGroupName());

        return ResponseEntity
                .ok()
                .body(Response.success(
                        PUT_SCRAP_GROUP_NAME_SUCCESS.getCode(),
                        PUT_SCRAP_GROUP_NAME_SUCCESS.getMessage(),
                        scrapGroups
                ));
    }

    @DeleteMapping("/delete")
    @MemberOnly
    public ResponseEntity<Response<ScrapGroupResponseDto>> deleteScrapGroup(@Auth Accessor accessor,
            @RequestBody ScrapGroupDeleteRequestDto requestDto)
    {
        Long memberId = accessor.memberId();
        ScrapGroupResponseDto scrapGroups = scrapGroupService.deleteScrapGroup(memberId, requestDto.getScrapGroupId());

        return ResponseEntity
                .ok()
                .body(Response.success(
                        DELETE_SCRAP_GROUP_SUCCESS.getCode(),
                        DELETE_SCRAP_GROUP_SUCCESS.getMessage(),
                        scrapGroups
                ));
    }
}