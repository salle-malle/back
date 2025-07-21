package com.shinhan.pda_midterm_project.presentation.scrap.dto;

import com.shinhan.pda_midterm_project.domain.scrap.model.Scrap;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScrapResponseDto {
    private final Long scrapId;
    private final Long memberStockSnapshotId;
    private final LocalDateTime createdAt;

    public ScrapResponseDto(Scrap scrap) {
        this.scrapId = scrap.getId();
        this.memberStockSnapshotId = scrap.getMemberStockSnapshot().getId();
        this.createdAt = scrap.getCreatedAt();
    }
}