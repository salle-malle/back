package com.shinhan.pda_midterm_project.domain.member_stock.repository;

import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member_stock.model.MemberStock;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberStockRepository extends JpaRepository<MemberStock, Long> {
  List<MemberStock> findByMember(Member member);

  @Query("SELECT ms FROM MemberStock ms JOIN FETCH ms.stock WHERE ms.member = :member")
  List<MemberStock> findByMemberWithStock(@Param("member") Member member);

  Optional<MemberStock> findByMemberAndStock(Member member, Stock stock);

  Optional<MemberStock> findByMemberAndStock_StockId(Member member, String stockId);
}