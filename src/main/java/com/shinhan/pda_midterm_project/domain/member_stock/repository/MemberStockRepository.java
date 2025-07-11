package com.shinhan.pda_midterm_project.domain.member_stock.repository;

import com.shinhan.pda_midterm_project.domain.member_stock.model.MemberStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberStockRepository extends JpaRepository<MemberStock, Long> {
}