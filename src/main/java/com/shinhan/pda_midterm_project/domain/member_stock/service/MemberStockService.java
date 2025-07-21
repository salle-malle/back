package com.shinhan.pda_midterm_project.domain.member_stock.service;

import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member_stock.model.MemberStock;

import java.util.List;

public interface MemberStockService {
  /**
   * 한국투자증권 API를 통해 주식 상세 정보를 업데이트
   */
  void updateStockDetailFromKis(String stockId, String accessToken, Member member);

  /**
   * 회원의 보유 주식 목록 조회
   */
  List<MemberStock> getMemberStocks(Member member);

  /**
   * 특정 종목의 보유 정보 조회
   */
  MemberStock getMemberStock(Member member, String stockId);
}
