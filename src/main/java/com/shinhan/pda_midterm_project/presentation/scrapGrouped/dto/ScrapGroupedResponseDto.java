// presentation/scrapGroup/dto/ScrapGroupResponseDto.java 경로에 파일 생성
package com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto;

import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.model.ScrapGrouped;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ScrapGroupedResponseDto {

    private final Long scrapGroupedId;
    private final Long scrapGroupId;
    private final String scrapGroupName;
    private final Long memberStockSnapshotId;
    private final String stockName;
    private final LocalDateTime createdAt;

    public ScrapGroupedResponseDto(ScrapGrouped scrapGrouped) {
        this.scrapGroupedId = scrapGrouped.getScrapGroupedId();
        this.scrapGroupId = scrapGrouped.getScrapGroup().getId();
        this.scrapGroupName = scrapGrouped.getScrapGroup().getScrapGroupName();
        this.memberStockSnapshotId = scrapGrouped.getMemberStockSnapshot().getId();
        this.stockName = scrapGrouped.getMemberStockSnapshot().getInvestmentTypeNewsComment().getSummary().getStock().getStockName();
        this.createdAt = scrapGrouped.getCreatedAt();
    }
}