package com.shinhan.pda_midterm_project.presentation.scrapGrouped.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // JSON 역직렬화를 위해 기본 생성자 추가
public class ScrapGroupedDeleteRequestDto {
    private Long scrapGroupId;
    private Long scrapGroupedId;
}