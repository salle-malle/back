package com.shinhan.pda_midterm_project.presentation.scrapGroup.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.scrap_group.service.ScrapGroupService;
import com.shinhan.pda_midterm_project.presentation.scrapGroup.dto.*;
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

    @GetMapping("")//OK
    @MemberOnly
    public ResponseEntity<Response<List<ScrapGroupResponseDto>>> getScrapGroup(@Auth Accessor accessor) {
        Long memberId = accessor.memberId();
        List<ScrapGroupResponseDto> scrapGroups = scrapGroupService.getScrapGroup(memberId);

        return ResponseEntity
                .ok()
                .body(Response.success(
                        GET_SCRAP_GROUP_SUCCESS.getCode(),
                        GET_SCRAP_GROUP_SUCCESS.getMessage(),
                        scrapGroups
                ));
    }

    @PostMapping("/push")//OK
    @MemberOnly
    public ResponseEntity<Response<ScrapGroupResponseDto>> createScrapGroup(@Auth Accessor accessor,
                                                                            @RequestBody ScrapGroupCreateRequestDto requestDto) {

        Long memberId = accessor.memberId();
        String scrapGroupTitle = requestDto.getScrapGroupName();
        ScrapGroupResponseDto scrapGroups = scrapGroupService.createScrapGroup(memberId, scrapGroupTitle);

        return ResponseEntity
                .ok()
                .body(Response.success(
                        POST_SCRAP_GROUPED_SUCCESS.getCode(),
                        POST_SCRAP_GROUPED_SUCCESS.getMessage(),
                        scrapGroups
                ));
    }

    @PutMapping("/groupnameupdate")//OK
    public ResponseEntity<Response<ScrapGroupResponseDto>> updateScrapGroupName(@Auth Accessor accessor,
            @RequestBody ScrapGroupNameRequestDto requestDto)
     {
        ScrapGroupResponseDto scrapGroups = scrapGroupService.updateScrapGroup(requestDto.getScrapGroupId(), requestDto.getScrapGroupId(),requestDto.getScrapGroupName());

        return ResponseEntity
                .ok()
                .body(Response.success(
                        PUT_SCRAP_GROUP_NAME_SUCCESS.getCode(),
                        PUT_SCRAP_GROUP_NAME_SUCCESS.getMessage(),
                        scrapGroups
                ));
    }

    @DeleteMapping("/delete")//OK
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

    @GetMapping("/status/{scrapId}")//OK
    @MemberOnly
    public ResponseEntity<Response<List<GroupInclusionStatusDto>>> getGroupStatusForScrap(
            @Auth Accessor accessor,
            @PathVariable Long scrapId) {

        Long memberId = accessor.memberId();
        List<GroupInclusionStatusDto> groupStatusList = scrapGroupService.getGroupInclusionStatus(memberId, scrapId);

        return ResponseEntity.ok().body(Response.success(GET_SCRAP_GROUP_STATUS_SUCCESS.getCode(), GET_SCRAP_GROUP_STATUS_SUCCESS.getMessage() ,groupStatusList));
    }
}