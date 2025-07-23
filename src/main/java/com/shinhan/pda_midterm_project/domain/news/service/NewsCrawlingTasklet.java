package com.shinhan.pda_midterm_project.domain.news.service;

import com.shinhan.pda_midterm_project.domain.news.model.News;
import com.shinhan.pda_midterm_project.domain.news.repository.NewsRepository;
import com.shinhan.pda_midterm_project.presentation.news.dto.NewsCrawlingDto;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.domain.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsCrawlingTasklet implements Tasklet {

    private final StockRepository stockRepository;
    private final NewsRepository newsRepository;
    private final WebClient webClient;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("뉴스 크롤링 Tasklet 시작");

        // 1. DB에서 모든 News 정보 조회
        List<Stock> stocks = stockRepository.findAll();
        if (stocks.isEmpty()) {
            log.warn("DB에 등록된 주식 정보가 없습니다. Tasklet을 종료합니다.");
            return RepeatStatus.FINISHED;
        }

        // Ticker 목록과 Stock 객체 맵 생성
        List<String> tickers = stocks.stream().map(Stock::getStockId).collect(Collectors.toList());
        Map<String, Stock> stockMap = stocks.stream().collect(Collectors.toMap(Stock::getStockId, Function.identity()));

        NewsCrawlingDto.Response crawlingResponse = webClient.post()
                .uri("/crawl-news")
                .bodyValue(new NewsCrawlingDto.Request(tickers))
                .retrieve()
                .bodyToMono(NewsCrawlingDto.Response.class)
                .block(); // 배치 작업이므로 동기(blocking) 방식으로 결과를 기다림

        if (crawlingResponse == null || crawlingResponse.getCrawlResults() == null) {
            log.error("크롤링 서버로부터 응답을 받지 못했습니다.");
            return RepeatStatus.FINISHED;
        }

        // 3. API 응답을 News 엔티티로 변환
        List<News> sourcesToSave = new ArrayList<>();
        crawlingResponse.getCrawlResults().forEach((ticker, articles) -> {
            Stock stock = stockMap.get(ticker);
            if (stock != null) {
                for (NewsCrawlingDto.CrawledArticle article : articles) {
                    sourcesToSave.add(News.builder()
                            .stock(stock)
                            .newsTitle(article.getNewsTitle())
                            .newsContent(article.getNewsContent())
                            .newsUri(article.getNewsUri())
                            .newsDate(article.getNewsDate())
                            .newsImage(article.getNewsImage())
                            .build());
                }
            }
        });

        // 4. DB에 저장
        newsRepository.saveAll(sourcesToSave);
        log.info("{}개의 새로운 뉴스 기사를 Source 테이블에 저장했습니다.", sourcesToSave.size());

        log.info("뉴스 크롤링 Tasklet 종료");
        return RepeatStatus.FINISHED;
    }
}