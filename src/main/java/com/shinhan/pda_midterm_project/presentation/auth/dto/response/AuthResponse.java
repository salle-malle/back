package com.shinhan.pda_midterm_project.presentation.auth.dto.response;

import com.shinhan.pda_midterm_project.domain.auth.model.AccessToken;

public class AuthResponse {
    public record ResponseToken(AccessToken accessToken) {
        public static ResponseToken of(AccessToken accessToken) {
            return new ResponseToken(accessToken);
        }
    }
}
