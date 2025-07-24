package com.shinhan.pda_midterm_project.domain.scrap.repository;

import com.shinhan.pda_midterm_project.domain.scrap.model.Scrap;
import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List; // ✅ List import
import java.util.Optional;
import java.util.Set;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    @Query("SELECT s.memberStockSnapshot.id FROM Scrap s WHERE s.member.id = :memberId AND s.memberStockSnapshot.id IN :snapshotIds")
    Set<Long> findScrappedSnapshotIdsByMemberIdAndSnapshotIds(@Param("memberId") Long memberId, @Param("snapshotIds") List<Long> snapshotIds);

    @Query("SELECT s.memberStockSnapshot.id FROM Scrap s WHERE s.member.id = :memberId AND s.memberStockSnapshot.id IN :snapshotId")

    Long findScrappedSnapshotIdByMemberIdAndSnapshotId(@Param("memberId") Long memberId, @Param("snapshotId") Long snapshotId);

    boolean existsByMemberIdAndMemberStockSnapshotId(Long memberId, Long snapshotId);

    /**
     * 특정 사용자가 특정 스냅샷을 스크랩한 Scrap 엔티티를 찾습니다.
     * @param memberId 사용자 ID
     * @param snapshotId 스냅샷 ID
     * @return Optional<Scrap>
     */
    Optional<Scrap> findByMemberIdAndMemberStockSnapshotId(Long memberId, Long snapshotId);
//    Optional<Scrap> findByMemberIdAndMemberStockSnapshotId(Long memberId, Long snapshotId);
}