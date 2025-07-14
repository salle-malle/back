package com.shinhan.pda_midterm_project.presentation.scrapGrouped.controller;

import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.service.ScrapGroupedService;
import com.shinhan.pda_midterm_project.presentation.scrapGroup.dto.ScrapGroupResponseDto;
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.shinhan.pda_midterm_project.common.response.ResponseMessages.GET_SCRAP_GROUPED_SUCCESS;

@RestController
@RequestMapping("/api/v1/scrapgrouped")
@RequiredArgsConstructor
public class ScrapGroupedController {
    private final ScrapGroupedService scrapGroupedService;

    @GetMapping("")
    public ResponseEntity<Response<List<ScrapGroupedResponseDto>>> getScrapedGroup() {
        Long memberId = 2L;
        Long scrapGroupId = 2L;

        List<ScrapGroupedResponseDto> scrapedItems = scrapGroupedService.getScrapGrouped(memberId, scrapGroupId);

        // 3. 성공 응답을 반환합니다.
        return ResponseEntity
                .ok()
                .body(Response.success(
                        GET_SCRAP_GROUPED_SUCCESS.getCode(),
                        GET_SCRAP_GROUPED_SUCCESS.getMessage(),
                        scrapedItems
                ));
    }
}