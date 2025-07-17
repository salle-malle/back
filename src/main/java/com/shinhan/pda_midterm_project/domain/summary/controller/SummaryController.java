package com.shinhan.pda_midterm_project.domain.summary.controller;

import com.shinhan.pda_midterm_project.domain.news.model.News;
import com.shinhan.pda_midterm_project.domain.news.repository.NewsRepository;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.domain.stock.repository.StockRepository;
import com.shinhan.pda_midterm_project.domain.summary.model.Summary;
import com.shinhan.pda_midterm_project.domain.summary.service.SummaryService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

//    private final SummaryService summaryService;
//    private final NewsRepository newsRepository;

    /**
     * 테스트용: 뉴스 요약 생성 & 저장
     */
//    @PostMapping("/generate-and-save")
//    public void generateSummaryForTodayNews() {
//        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
//        LocalDateTime endOfDay = startOfDay.plusDays(1);
//
//        List<News> todayNews = newsRepository.findAllByCreatedAtBetween(startOfDay, endOfDay);
//
//        // 종목별로 뉴스 그룹핑
//        Map<Stock, List<News>> newsByStock = todayNews.stream()
//                .filter(n -> n.getStock() != null)
//                .collect(Collectors.groupingBy(News::getStock));
//
//        for (Map.Entry<Stock, List<News>> entry : newsByStock.entrySet()) {
//            Stock stock = entry.getKey();
//            List<News> newsList = entry.getValue();
//
//            // 뉴스 내용 합치기
//            String combinedContent = newsList.stream()
//                    .map(News::getNewsContent)
//                    .collect(Collectors.joining("\n\n"));
//
//            // 요약 및 저장
//            summaryService.summarizeAndSave(combinedContent, stock);
//        }
//    }

    /**
     * 테스트용: 투자 성향별 첨언 생성
     */
//    @PostMapping("/commentary")
//    public String generateCommentary(@RequestParam String type, @RequestBody String summaryContent) {
//        return summaryService.generateCommentary(summaryContent, type);
//    }
}
