// domain/scrap_group/service/ScrapGroupedService.java 경로에 파일 생성
package com.shinhan.pda_midterm_project.domain.scrap_grouped.service;

import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import com.shinhan.pda_midterm_project.presentation.scrapGroup.dto.ScrapGroupResponseDto;
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedDeleteRequestDto;
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedPushRequestDto;
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedResponseDto;

import java.util.List;

public interface ScrapGroupedService {
    List<ScrapGroupedResponseDto> getScrapGrouped(Long memberId,Long scrapGroupId);

    /**
     * 사용자 ID로 스크랩 그룹 목록을 조회합니다.e
     * @param memberId 사용자 ID
     * @return 스크랩 그룹 DTO 목록
     */
    //List<ScrapGroupResponseDto> getScrapedGroup(Long memberId);
    ScrapGroupedResponseDto pushScrap(Long memberId, ScrapGroupedPushRequestDto requestDto);

    ScrapGroupedResponseDto deleteScrap(Long memberId, Long scrapGroupedId);
}