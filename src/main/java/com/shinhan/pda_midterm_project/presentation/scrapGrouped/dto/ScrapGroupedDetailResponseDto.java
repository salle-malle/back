package com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto;

import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.model.ScrapGrouped;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScrapGroupedDetailResponseDto {

    // 스냅샷 관련 정보
    private final boolean isScrap;
    private final Long snapshotId;
    private final LocalDateTime snapshotCreatedAt;
    private final String personalizedComment;
    private final String stockCode;
    private final String stockName;
    private final String newsContent;
    private final String newsImage;
    private final Long scrapGroupedId;

    // 그룹 관련 정보 (필요시 포함)
    private final Long scrapGroupId;
    private final String scrapGroupName;

    public ScrapGroupedDetailResponseDto(ScrapGrouped scrapGrouped) {
        MemberStockSnapshot snapshot = scrapGrouped.getScrap().getMemberStockSnapshot();

        // 이 API는 특정 그룹에 '포함된' 스크랩을 조회하므로 isScrap은 항상 true입니다.
        this.isScrap = true;
        this.snapshotId = snapshot.getId();
        this.snapshotCreatedAt = snapshot.getCreatedAt();

        // 연관된 엔티티를 통해 상세 정보에 접근합니다.
        this.personalizedComment = snapshot.getInvestmentTypeNewsComment().getInvestmentTypeNewsContent();
        this.newsContent = snapshot.getInvestmentTypeNewsComment().getSummary().getNewsContent();
        this.newsImage = snapshot.getInvestmentTypeNewsComment().getSummary().getNewsImage();
        this.stockCode = snapshot.getInvestmentTypeNewsComment().getSummary().getStock().getStockId();
        this.stockName = snapshot.getInvestmentTypeNewsComment().getSummary().getStock().getStockName();

        // 그룹 정보
        this.scrapGroupId = scrapGrouped.getScrapGroup().getId();
        this.scrapGroupName = scrapGrouped.getScrapGroup().getScrapGroupName();
        this.scrapGroupedId = scrapGrouped.getId();
    }
}