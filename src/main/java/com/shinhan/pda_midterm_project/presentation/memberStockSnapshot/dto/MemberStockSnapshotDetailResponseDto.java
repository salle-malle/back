package com.shinhan.pda_midterm_project.presentation.memberStockSnapshot.dto;

import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberStockSnapshotDetailResponseDto {

    // MemberStockSnapshot 정보
    private final Long snapshotId;
    private final LocalDateTime snapshotCreatedAt;

    // InvestmentTypeNewsComment 정보
    private final String personalizedComment;

    // Stock 정보 (Summary -> Stock)
    private final String stockCode;
    private final String stockName;

    // News 정보 (Summary -> News)
    private final String newsContent;
//    private final String newsTitle;
//    private final String newsSourceUrl;
//    private final LocalDateTime newsPublishedAt;

    public MemberStockSnapshotDetailResponseDto(MemberStockSnapshot snapshot) {
        this.snapshotId = snapshot.getId();
        this.snapshotCreatedAt = snapshot.getCreatedAt();

        // 1. 스냅샷에서 '투자 성향별 뉴스 코멘트' 엔티티를 가져옵니다.
        var investmentComment = snapshot.getInvestmentTypeNewsComment();
        this.personalizedComment = investmentComment.getInvestmentTypeNewsContent();

        // 2. [수정] 코멘트 엔티티를 통해 '요약(Summary)' 엔티티에 접근합니다.
        // ERD 경로: MemberStockSnapshot -> InvestmentTypeNewsComment -> Summary
        var summary = investmentComment.getSummary();

        // 3. [수정] 요약(Summary) 엔티티를 통해 '종목(Stock)' 엔티티에 접근하여 정보를 가져옵니다.
        // ERD 경로: Summary -> Stock
        var stock = summary.getStock();
        this.stockCode = stock.getStockId();
        this.stockName = stock.getStockName();

        // 4. [수정] 요약(Summary) 엔티티를 통해 '뉴스(News)' 엔티티에 접근하여 정보를 가져옵니다.
        // ERD 경로: Summary -> News

        this.newsContent = summary.getNewsContent();
//        this.newsTitle = summary.getNewsContent();
//        this.newsSourceUrl = summary.get();
//        this.newsPublishedAt = news.getNewsDate();
    }
}