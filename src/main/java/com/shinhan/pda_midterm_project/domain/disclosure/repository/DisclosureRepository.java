package com.shinhan.pda_midterm_project.domain.disclosure.repository;

import com.shinhan.pda_midterm_project.domain.disclosure.model.Disclosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisclosureRepository extends JpaRepository<Disclosure, Long> {
}