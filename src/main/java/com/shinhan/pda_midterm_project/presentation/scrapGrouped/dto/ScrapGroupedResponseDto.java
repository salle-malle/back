package com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto;

import com.shinhan.pda_midterm_project.domain.scrap_grouped.model.ScrapGrouped;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScrapGroupedResponseDto {
    private final Long id;
    private final Long scrapId;
    private final Long scrapGroupId;
    private final String scrapGroupName;
    private final Long memberStockSnapshotId;
    private final String stockName;
    private final LocalDateTime createdAt;

    public ScrapGroupedResponseDto(ScrapGrouped scrapGrouped) {
        this.id = scrapGrouped.getId();
        this.scrapId = scrapGrouped.getScrap().getId(); // scrapId 추가
        this.scrapGroupId = scrapGrouped.getScrapGroup().getId();
        this.scrapGroupName = scrapGrouped.getScrapGroup().getScrapGroupName();
        // scrap을 한 번 더 거쳐서 스냅샷 정보에 접근
        this.memberStockSnapshotId = scrapGrouped.getScrap().getMemberStockSnapshot().getId();
        this.stockName = scrapGrouped.getScrap().getMemberStockSnapshot().getInvestmentTypeNewsComment().getSummary().getStock().getStockName();
        this.createdAt = scrapGrouped.getCreatedAt();
    }
}