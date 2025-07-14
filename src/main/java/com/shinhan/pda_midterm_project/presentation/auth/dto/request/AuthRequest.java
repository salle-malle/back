package com.shinhan.pda_midterm_project.presentation.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AuthRequest {

    public record Login(
            @NotBlank(message = "ID는 공백이 될 수 없습니다")
            String id,

            @NotBlank(message = "PW는 공백이 될 수 없습니다")
            String password
    ) {
        public static Login of(String id, String password) {
            return new Login(id, password);
        }
    }
}
