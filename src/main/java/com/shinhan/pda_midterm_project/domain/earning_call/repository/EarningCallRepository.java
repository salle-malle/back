package com.shinhan.pda_midterm_project.domain.earning_call.repository;

import com.shinhan.pda_midterm_project.domain.earning_call.model.EarningCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EarningCallRepository extends JpaRepository<EarningCall, Long> {

  /**
   * 특정 주식 ID로 어닝콜 데이터 조회
   */
  List<EarningCall> findByStockStockId(String stockId);

  /**
   * 특정 날짜로 어닝콜 데이터 조회
   */
  List<EarningCall> findByEarningCallDate(String earningCallDate);

  /**
   * 사용자 보유종목의 어닝콜 데이터 조회
   */
  @Query("SELECT ec FROM EarningCall ec " +
      "JOIN ec.stock s " +
      "JOIN MemberStock ms ON ms.stock = s " +
      "WHERE ms.member.id = :memberId")
  List<EarningCall> findByMemberId(@Param("memberId") Long memberId);

  /**
   * 특정 주식 ID 목록으로 어닝콜 데이터 조회
   */
  @Query("SELECT ec FROM EarningCall ec " +
      "WHERE ec.stock.stockId IN :stockIds")
  List<EarningCall> findByStockIds(@Param("stockIds") List<String> stockIds);
}