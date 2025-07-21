package com.shinhan.pda_midterm_project.presentation.scrapGroup.dto;

import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import lombok.Getter;

@Getter
public class GroupInclusionStatusDto {
    private Long groupId;
    private String groupName;
    private boolean isAlreadyIncluded; // 이 스크랩이 이미 포함되었는지 여부

    public GroupInclusionStatusDto(ScrapGroup scrapGroup, boolean isAlreadyIncluded) {
        this.groupId = scrapGroup.getId();
        this.groupName = scrapGroup.getScrapGroupName();
        this.isAlreadyIncluded = isAlreadyIncluded;
    }
}