package com.shinhan.pda_midterm_project.domain.member_stock_snapshot.repository;

import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberStockSnapshotRepository extends JpaRepository<MemberStockSnapshot, Long> {
    List<MemberStockSnapshot> findAllByMemberId(Long memberId);

    @Query("SELECT mss FROM MemberStockSnapshot mss " +
            "JOIN FETCH mss.member " +
            "JOIN FETCH mss.investmentTypeNewsComment itnc " +
            "JOIN FETCH itnc.summary s " +
            "JOIN FETCH s.stock " +
            "WHERE mss.id = :snapshotId")
    Optional<MemberStockSnapshot> findCardDetailById(@Param("snapshotId") Long snapshotId);

    @Query(value = "SELECT mss FROM MemberStockSnapshot mss " +
            "JOIN FETCH mss.investmentTypeNewsComment itnc " +
            "JOIN FETCH itnc.summary s " +
            "JOIN FETCH s.stock " +
            "WHERE mss.member.id = :memberId",
            countQuery = "SELECT COUNT(mss) FROM MemberStockSnapshot mss WHERE mss.member.id = :memberId")
    Page<MemberStockSnapshot> findCardsByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT mss FROM MemberStockSnapshot mss " +
            "JOIN FETCH mss.investmentTypeNewsComment itnc " +
            "JOIN FETCH itnc.summary s " +
            "JOIN FETCH s.stock " +
            "WHERE mss.member.id = :memberId AND CAST(mss.createdAt AS date) = :date")
    List<MemberStockSnapshot> findCardsByMemberIdAndDate(@Param("memberId") Long memberId, @Param("date") java.sql.Date date);

    @Query("SELECT mss FROM MemberStockSnapshot mss " +
            "JOIN FETCH mss.investmentTypeNewsComment itnc " +
            "JOIN FETCH itnc.summary sum " +
            "JOIN FETCH sum.stock st " +
            // "JOIN FETCH sum.news n " +  <-- 이 부분을 삭제했습니다.
            "WHERE st.stockId = :stockCode AND mss.id IN (" +
            "  SELECT s.memberStockSnapshot.id FROM Scrap s WHERE s.member.id = :memberId" +
            ") " +
            "ORDER BY mss.createdAt DESC")
    List<MemberStockSnapshot> findScrappedCardsByMemberIdAndStockCode(@Param("memberId") Long memberId, @Param("stockCode") String stockCode);

    @Query("SELECT mss FROM MemberStockSnapshot mss " +
            "JOIN FETCH mss.investmentTypeNewsComment itnc " +
            "JOIN FETCH itnc.summary s " +
            "JOIN FETCH s.stock " +
            "WHERE mss.member.id = :memberId " +
            // createdAt 필드를 DATE 타입으로 변환하여 시간 정보 없이 날짜만 비교
            "AND CAST(mss.createdAt AS date) BETWEEN :startDate AND :endDate " +
            "ORDER BY mss.createdAt DESC")
    List<MemberStockSnapshot> findCardsByMemberIdAndCreatedAtBetween(
            @Param("memberId") Long memberId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
    /**
     * 특정 사용자가 스크랩한 스냅샷만 날짜 기준으로 조회합니다.
     * (이전 답변에서 LocalDate로 수정한 버전)
     */
    @Query("SELECT mss FROM MemberStockSnapshot mss " +
            "JOIN Scrap s ON s.memberStockSnapshot.id = mss.id " +
            "JOIN FETCH mss.investmentTypeNewsComment itnc " +
            "JOIN FETCH itnc.summary sum " +
            "JOIN FETCH sum.stock " +
            "WHERE s.member.id = :memberId AND FUNCTION('DATE', mss.createdAt) = :date " +
            "ORDER BY mss.createdAt DESC")
    List<MemberStockSnapshot> findScrappedCardsByMemberIdAndDate(@Param("memberId") Long memberId, @Param("date") java.sql.Date date);
}