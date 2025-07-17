package com.shinhan.pda_midterm_project.domain.stock.repository;

import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {
  Optional<Stock> findByOvrsPdno(String ovrsPdno);
}