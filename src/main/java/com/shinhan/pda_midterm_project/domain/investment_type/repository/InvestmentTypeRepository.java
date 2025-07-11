package com.shinhan.pda_midterm_project.domain.investment_type.repository;

import com.shinhan.pda_midterm_project.domain.investment_type.model.InvestmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentTypeRepository extends JpaRepository<InvestmentType, Long> {
}