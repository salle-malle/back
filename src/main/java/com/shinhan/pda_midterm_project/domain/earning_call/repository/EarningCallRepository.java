package com.shinhan.pda_midterm_project.domain.earning_call.repository;

import com.shinhan.pda_midterm_project.domain.earning_call.model.EarningCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EarningCallRepository extends JpaRepository<EarningCall, Long> {
}