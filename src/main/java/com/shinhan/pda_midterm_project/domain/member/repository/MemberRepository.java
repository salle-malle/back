package com.shinhan.pda_midterm_project.domain.member.repository;

import com.shinhan.pda_midterm_project.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
