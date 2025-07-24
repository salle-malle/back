// ScrapService.java (Interface)
package com.shinhan.pda_midterm_project.domain.scrap.service;

import com.shinhan.pda_midterm_project.presentation.scrap.dto.ScrapResponseDto;
import com.shinhan.pda_midterm_project.presentation.scrap.dto.ScrapStatusResponse;

public interface ScrapService {
    ScrapResponseDto createScrap(Long memberId, Long snapshotId);
    void deleteScrap(Long memberId, Long scrapId);
    ScrapStatusResponse getScrapStatus(Long memberId, Long snapshotId);
    void deleteScrapBySnapshotId(Long memberId, Long snapshotId);
}