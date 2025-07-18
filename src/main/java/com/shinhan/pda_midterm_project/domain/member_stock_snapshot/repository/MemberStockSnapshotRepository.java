package com.shinhan.pda_midterm_project.domain.member_stock_snapshot.repository;

import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberStockSnapshotRepository extends JpaRepository<MemberStockSnapshot, Long> {
    List<MemberStockSnapshot> findAllByMemberId(Long memberId);
}