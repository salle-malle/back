package com.shinhan.pda_midterm_project.domain.auth.service;

import com.shinhan.pda_midterm_project.domain.auth.model.UserTokens;

public interface AuthService {
    UserTokens login(String id, String password);
}
