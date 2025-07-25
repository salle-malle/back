// ScrapGroupedService.java (Interface) - push, delete 메서드의 파라미터 변경
package com.shinhan.pda_midterm_project.domain.scrap_grouped.service;

import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedDetailResponseDto;
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedPushRequestDto;
import com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto.ScrapGroupedResponseDto;
import java.util.List;

public interface ScrapGroupedService {
    List<ScrapGroupedDetailResponseDto> getScrapsDetailInGroup(Long memberId, Long scrapGroupId);
    List<ScrapGroupedResponseDto> getScrapsInGroup(Long memberId, Long scrapGroupId);
    ScrapGroupedResponseDto addScrapToGroup(Long memberId, ScrapGroupedPushRequestDto requestDto);
    void removeScrapFromGroup(Long memberId, Long scrapGroupedId);
}