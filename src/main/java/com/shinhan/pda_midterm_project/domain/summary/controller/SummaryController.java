package com.shinhan.pda_midterm_project.domain.summary.controller;

import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.domain.stock.repository.StockRepository;
import com.shinhan.pda_midterm_project.domain.summary.model.Summary;
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
    private final StockRepository stockRepository;

    /**
     * 테스트용: 뉴스 요약 생성 & 저장
     */
    @PostMapping("/generate-and-save")
    public Summary generateAndSave(
            @RequestParam String stockId,
            @RequestBody String content
    ) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("해당 종목이 존재하지 않습니다."));

        return summaryService.summarizeAndSave(content, stock);
    }

    /**
     * 테스트용: 투자 성향별 첨언 생성
     */
    @PostMapping("/commentary")
    public String generateCommentary(@RequestParam String type, @RequestBody String summaryContent) {
        return summaryService.generateCommentary(summaryContent, type);
    }
}
