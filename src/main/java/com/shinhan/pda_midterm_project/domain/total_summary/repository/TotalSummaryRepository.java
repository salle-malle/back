package com.shinhan.pda_midterm_project.domain.total_summary.repository;

import com.shinhan.pda_midterm_project.domain.total_summary.model.TotalSummary;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalSummaryRepository extends JpaRepository<TotalSummary, Long> {

    @Query("SELECT t.totalContent FROM TotalSummary t WHERE t.member.id = :memberId AND t.createdAt BETWEEN :start AND :end")
    String getTodayTotalSummary(@Param("memberId") Long memberId,
                                @Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end);


}
