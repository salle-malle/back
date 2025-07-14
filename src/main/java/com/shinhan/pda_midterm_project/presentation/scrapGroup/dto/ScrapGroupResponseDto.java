// presentation/scrapGroup/dto/ScrapGroupResponseDto.java 경로에 파일 생성
package com.shinhan.pda_midterm_project.presentation.scrapGroup.dto;

import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScrapGroupResponseDto {

    private final Long id;
    private final String scrapGroupName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ScrapGroupResponseDto(ScrapGroup scrapGroup) {
        this.id = scrapGroup.getId();
        this.scrapGroupName = scrapGroup.getScrapGroupName();
        this.createdAt = scrapGroup.getCreatedAt();
        this.updatedAt = scrapGroup.getUpdatedAt();
    }
}