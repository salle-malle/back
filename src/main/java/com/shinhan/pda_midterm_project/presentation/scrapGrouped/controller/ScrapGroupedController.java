package com.shinhan.pda_midterm_project.presentation.scrapGrouped.controller;

import com.shinhan.pda_midterm_project.common.response.Response;
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
    public ResponseEntity<Response<List<ScrapGroupedResponseDto>>> getScrapedGroup() {
        Long memberId = 2L;
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
    public ResponseEntity<Response<ScrapGroupedResponseDto>> pushScrapedGroup( //멤버아이디, 스냅샷, 스크랩그룹아이디
            @RequestBody ScrapGroupedPushRequestDto requestDto) {
        Long memberId = 2L;

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
    public ResponseEntity<Response<ScrapGroupedResponseDto>> deleteScrapedGroup( //멤버아이디, 스냅샷, 스크랩그룹아이디
                                                                               @RequestBody ScrapGroupedDeleteRequestDto requestDto) {
        Long memberId = 2L;

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