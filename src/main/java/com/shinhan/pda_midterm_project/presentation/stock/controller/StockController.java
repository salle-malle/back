package com.shinhan.pda_midterm_project.presentation.stock.controller;

import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.domain.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {

  private final StockRepository stockRepository;

  /**
   * Stock 데이터 생성 (테스트용)
   */
  @PostMapping("/create")
  public ResponseEntity<Response<String>> createStock(@RequestParam String stockId, @RequestParam String stockName) {
    try {
      Stock stock = Stock.builder()
          .stockId(stockId)
          .stockName(stockName)
          .build();

      stockRepository.save(stock);
      return ResponseEntity.ok(Response.success(
          ResponseMessages.SUCCESS.getCode(),
          "Stock 데이터가 성공적으로 생성되었습니다.",
          null));
    } catch (Exception e) {
      log.error("Error creating stock, e");
      return ResponseEntity.badRequest()
          .body(Response.failure(
              ResponseMessages.API_ERROR.getCode(),
              "Stock 데이터 생성 중 오류가 발생했습니다: " + e.getMessage()));
    }
  }

  /**
   * 모든 Stock 데이터 조회
   */
  @GetMapping
  public ResponseEntity<Response<List<Stock>>> getAllStocks() {
    try {
      List<Stock> stocks = stockRepository.findAll();
      return ResponseEntity.ok(Response.success(
          ResponseMessages.SUCCESS.getCode(),
          "Stock 데이터 조회를 성공했습니다.",
          stocks));
    } catch (Exception e) {
      log.error("Error retrieving stocks, e");
      return ResponseEntity.badRequest()
          .body(Response.failure(
              ResponseMessages.API_ERROR.getCode(),
              "Stock 데이터 조회 중 오류가 발생했습니다: " + e.getMessage()));
    }
  }
}