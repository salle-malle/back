package com.shinhan.pda_midterm_project.presentation.scrapGrouped.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.service.ScrapGroupedService;
import com.shinhan.pda_midterm_project.presentation.scrapGroup.dto.ScrapGroupResponseDto;
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

    @GetMapping("")
    @MemberOnly
    public ResponseEntity<Response<List<ScrapGroupedResponseDto>>> getScrapedGroup(@Auth Accessor accessor) {
        Long memberId = accessor.memberId();
        Long scrapGroupId = 2L;

        List<ScrapGroupedResponseDto> scrapedItems = scrapGroupedService.getScrapGrouped(memberId, scrapGroupId);

        return ResponseEntity
                .ok()
                .body(Response.success(
                        GET_SCRAP_GROUPED_SUCCESS.getCode(),
                        GET_SCRAP_GROUPED_SUCCESS.getMessage(),
                        scrapedItems
                ));
    }

    @PostMapping("/push")
    @MemberOnly
    public ResponseEntity<Response<ScrapGroupedResponseDto>> pushScrapedGroup(@Auth Accessor accessor,
            @RequestBody ScrapGroupedPushRequestDto requestDto) {
        Long memberId = accessor.memberId();

        // 2. Service를 호출하여 스크랩 추가 로직 수행
        ScrapGroupedResponseDto newScrapedItem = scrapGroupedService.pushScrap(memberId, requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED) // 새로운 리소스가 성공적으로 생성되었음을 의미
                .body(Response.success(
                        CREATE_SCRAP_GROUPED_SUCCESS.getCode(),
                        CREATE_SCRAP_GROUPED_SUCCESS.getMessage(),
                        newScrapedItem
                ));
    }

    @DeleteMapping("/delete")
    @MemberOnly
    public ResponseEntity<Response<ScrapGroupedResponseDto>> deleteScrapedGroup(@Auth Accessor accessor,
                                                                               @RequestBody ScrapGroupedDeleteRequestDto requestDto) {
        Long memberId = accessor.memberId();

        ScrapGroupedResponseDto deleteScrapedItem = scrapGroupedService.deleteScrap(memberId, requestDto.getScrapGroupedId());

        return ResponseEntity
                .status(HttpStatus.CREATED) // 새로운 리소스가 성공적으로 생성되었음을 의미
                .body(Response.success(
                        DELETE_SCRAP_GROUPED_SUCCESS.getCode(),
                        DELETE_SCRAP_GROUPED_SUCCESS.getMessage(),
                        deleteScrapedItem
                ));
    }
}