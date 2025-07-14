package com.shinhan.pda_midterm_project.domain.auth.model;

public record UserTokens(AccessToken accessToken) {
    public static UserTokens of(AccessToken accessToken) {
        return new UserTokens(accessToken);
    }
}
