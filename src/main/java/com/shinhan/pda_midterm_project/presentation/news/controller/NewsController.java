package com.shinhan.pda_midterm_project.presentation.news.controller;

import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.news.service.NewsService;
import com.shinhan.pda_midterm_project.presentation.news.dto.StockNewsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

  private final NewsService newsService;

  /**
   * 종목별 관련 뉴스 조회
   * 
   * @param stockId 종목코드
   * @return 뉴스 목록
   */
  @GetMapping("/stock/{stockId}")
  public ResponseEntity<Response<List<StockNewsResponseDto>>> getNewsByStockId(@PathVariable String stockId) {
    try {
      log.info("종목별 뉴스 조회 요청: stockId={}", stockId);

      List<StockNewsResponseDto> newsList = newsService.getNewsByStockId(stockId);

      return ResponseEntity.ok(Response.success(
          ResponseMessages.SUCCESS.getCode(),
          "종목별 뉴스 조회를 성공했습니다.",
          newsList));
    } catch (Exception e) {
      log.error("종목별 뉴스 조회 중 오류 발생: stockId={}, error={}", stockId, e.getMessage(), e);
      return ResponseEntity.badRequest()
          .body(Response.failure(
              ResponseMessages.API_ERROR.getCode(),
              "종목별 뉴스 조회 중 오류가 발생했습니다: " + e.getMessage()));
    }
  }
}