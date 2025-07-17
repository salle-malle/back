package com.shinhan.pda_midterm_project.domain.earning_call.service;

import com.shinhan.pda_midterm_project.domain.earning_call.model.EarningCall;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EarningCallService {

  /**
   * CSV 파일을 파싱하여 어닝콜 데이터를 데이터베이스에 저장
   */
  void parseAndSaveEarningCalls(MultipartFile csvFile);

  /**
   * 모든 어닝콜 데이터 조회
   */
  List<EarningCall> getAllEarningCalls();

  /**
   * 특정 주식의 어닝콜 데이터 조회
   */
  List<EarningCall> getEarningCallsByStockId(String stockId);

  /**
   * 특정 날짜의 어닝콜 데이터 조회
   */
  List<EarningCall> getEarningCallsByDate(String date);

  /**
   * 사용자 보유종목의 어닝콜 데이터 조회
   */
  List<EarningCall> getEarningCallsByMemberId(Long memberId);
}