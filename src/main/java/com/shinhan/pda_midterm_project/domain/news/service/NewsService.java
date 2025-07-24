package com.shinhan.pda_midterm_project.domain.news.service;

import com.shinhan.pda_midterm_project.common.util.SseEmitterRepository;
import com.shinhan.pda_midterm_project.domain.news.repository.NewsRepository;
import com.shinhan.pda_midterm_project.domain.news.model.News;
import com.shinhan.pda_midterm_project.presentation.news.dto.StockNewsResponseDto;
import com.shinhan.pda_midterm_project.domain.notification.model.Notification;
import com.shinhan.pda_midterm_project.domain.notification.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    /**
     * 종목코드로 관련 뉴스 조회
     * 
     * @param stockId 종목코드
     * @return 뉴스 목록
     */
    public List<StockNewsResponseDto> getNewsByStockId(String stockId) {
        List<News> newsList = newsRepository.findByStock_StockIdOrderByCreatedAtDesc(stockId);

        return newsList.stream()
                .map(news -> StockNewsResponseDto.builder()
                        .id(news.getId())
                        .newsTitle(news.getNewsTitle())
                        .newsContent(news.getNewsContent())
                        .newsUri(news.getNewsUri())
                        .newsDate(news.getNewsDate())
                        .newsImage(news.getNewsImage())
                        .createdAt(news.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
