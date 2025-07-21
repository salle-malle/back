package com.shinhan.pda_midterm_project.domain.main_news.service;

import com.shinhan.pda_midterm_project.common.util.TimeUtil;
import com.shinhan.pda_midterm_project.domain.main_news.model.MainNews;
import com.shinhan.pda_midterm_project.domain.main_news.repository.MainNewsRepository;
import com.shinhan.pda_midterm_project.presentation.news.dto.NewsCrawlingDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MainNewsCrawlingTasklet implements Tasklet {
    private final MainNewsRepository mainNewsRepository;
    private final WebClient webClient;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        log.info("뉴스 크롤링 Tasklet 시작");
        NewsCrawlingDto.Response response = webClient.get()
                .uri("/market/crawl_news")
                .retrieve()
                .bodyToMono(NewsCrawlingDto.Response.class)
                .block();

        if (response == null) {
            log.warn("크롤링 결과가 없거나 비어있습니다.");
            return RepeatStatus.FINISHED;
        }
        System.out.println(response);
        List<MainNews> mainNewsList = response.getCrawlResults()
                .get("yahoo_finance_market_news")
                .stream().map((result) -> MainNews.create(
                        result.getNewsTitle(),
                        result.getNewsContent(),
                        result.getNewsUri(),
                        TimeUtil.parseToLocalDateTime(result.getNewsDate())
                )).toList();

        mainNewsRepository.saveAll(mainNewsList);
        return RepeatStatus.FINISHED;
    }
}
