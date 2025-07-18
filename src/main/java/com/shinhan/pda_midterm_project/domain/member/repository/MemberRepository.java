package com.shinhan.pda_midterm_project.domain.member.repository;

import com.shinhan.pda_midterm_project.domain.member.model.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);

    // MemberRepository.java
    @Query("SELECT ms.member FROM MemberStock ms WHERE ms.stock.stockId = :stockId")
    List<Member> findAllByStockId(@Param("stockId") String stockId);

}
