package com.shinhan.pda_midterm_project.presentation.scrapGroup.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // JSON 역직렬화를 위해 기본 생성자 추가
public class ScrapGroupNameRequestDto {
    private Long scrapGroupId;
    private String scrapGroupName;
}