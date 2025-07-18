package com.shinhan.pda_midterm_project.test;

import com.shinhan.pda_midterm_project.domain.member.model.Member;

public interface testService {
    Member findById(Long memberId);

    void updatePhoneNumber(Long memberId, String phoneNumber);
}
