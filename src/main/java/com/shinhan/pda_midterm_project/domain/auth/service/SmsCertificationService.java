package com.shinhan.pda_midterm_project.domain.auth.service;

public interface SmsCertificationService {

    void issueCertificationSms(String phone);

    void verifyNumber(String expectedCode, String phoneNumber);
}
