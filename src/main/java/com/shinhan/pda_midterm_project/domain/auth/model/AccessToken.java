package com.shinhan.pda_midterm_project.domain.auth.model;

public record AccessToken(String accessToken) {
    public static AccessToken of(String accessToken) {
        return new AccessToken(accessToken);
    }
}
