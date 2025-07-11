package com.shinhan.pda_midterm_project.domain.investment_type_news_comment.repository;

import com.shinhan.pda_midterm_project.domain.investment_type_news_comment.model.InvestmentTypeNewsComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentTypeNewsCommentRepository extends JpaRepository<InvestmentTypeNewsComment, Long> {
}