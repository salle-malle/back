package com.shinhan.pda_midterm_project.domain.summary.controller;

import com.shinhan.pda_midterm_project.domain.summary.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/summary")
public class SummaryController {

    private final SummaryService summaryService;

    /**
     * 테스트용: 뉴스 요약 생성
     */
    @PostMapping("/generate")
    public String summarize(@RequestBody String content) {
        return summaryService.summarize(content);
    }

    /**
     * 테스트용: 투자 성향별 첨언 생성
     */
    @PostMapping("/commentary")
    public String generateCommentary(@RequestParam String type, @RequestBody String summaryContent) {
        return summaryService.generateCommentary(summaryContent, type);
    }
}
