// domain/scrap_group/service/ScrapGroupedService.java 경로에 파일 생성
package com.shinhan.pda_midterm_project.domain.scrap_group.service;

import com.shinhan.pda_midterm_project.presentation.scrapGroup.dto.ScrapGroupResponseDto;
import java.util.List;

public interface ScrapGroupService {

    /**
     * 사용자 ID로 스크랩 그룹 목록을 조회합니다.
     * @param memberId 사용자 ID
     * @return 스크랩 그룹 DTO 목록
     */
    List<ScrapGroupResponseDto> getScrapGroup(Long memberId);

    ScrapGroupResponseDto createScrapGroup(Long memberId, String ScrapGroupName);
}