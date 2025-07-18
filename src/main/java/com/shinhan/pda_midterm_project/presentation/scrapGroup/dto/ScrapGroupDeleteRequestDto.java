// 예: presentation/scrapGroup/dto/ScrapGroupDeleteRequestDto.java
package com.shinhan.pda_midterm_project.presentation.scrapGroup.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScrapGroupDeleteRequestDto {
    private Long scrapGroupId; // JSON의 키 이름과 변수 이름을 일치시킵니다.
}