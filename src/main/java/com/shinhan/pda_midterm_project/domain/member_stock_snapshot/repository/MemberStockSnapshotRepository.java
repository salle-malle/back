package com.shinhan.pda_midterm_project.domain.member_stock_snapshot.repository;

import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
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

    /**
     * 특정 사용자의 모든 스냅샷(카드) 목록을 페이지네이션하여 조회합니다.
     * N+1 문제를 방지하기 위해 Fetch Join을 사용합니다.
     * @param memberId 사용자 ID
     * @param pageable 페이지 정보 (size, page, sort)
     * @return MemberStockSnapshot 페이지 객체 (모든 연관 데이터가 채워진 상태)
     */
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
}