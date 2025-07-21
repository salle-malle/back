package com.shinhan.pda_midterm_project.presentation.mainNews.controller;

import static com.shinhan.pda_midterm_project.common.response.ResponseMessages.GET_CURRENT_MAIN_NEWS_SUCCESS;

import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.domain.main_news.model.MainNews;
import com.shinhan.pda_midterm_project.domain.main_news.service.MainNewsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/main-news")
public class MainNewsController {
    private final MainNewsService mainNewsService;

    @GetMapping("/current")
    public ResponseEntity<Response<List<MainNews>>> getCurrentMainNews() {
        List<MainNews> currentMainNews = mainNewsService.getCurrentMainNews();
        return ResponseEntity
                .ok()
                .body(Response.success(
                        GET_CURRENT_MAIN_NEWS_SUCCESS.getCode(),
                        GET_CURRENT_MAIN_NEWS_SUCCESS.getMessage(),
                        currentMainNews
                ));
    }
}
