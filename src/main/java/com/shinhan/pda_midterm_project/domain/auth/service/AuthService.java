package com.shinhan.pda_midterm_project.domain.auth.service;

import org.springframework.http.ResponseCookie;

public interface AuthService {
    ResponseCookie login(String id, String password);

    ResponseCookie signUp(String id, String password, String phoneNumber, String appKey, String appSecret,
                          String accountNumber);
}
