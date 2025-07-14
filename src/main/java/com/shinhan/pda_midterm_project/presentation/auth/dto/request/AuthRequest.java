package com.shinhan.pda_midterm_project.presentation.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AuthRequest {

    public record Login(
            @NotBlank(message = "ID는 공백이 될 수 없습니다")
            String id,

            @NotBlank(message = "PW는 공백이 될 수 없습니다")
            String password
    ) {
    }

    public record SignUp(
            @NotBlank(message = "ID는 공백이 될 수 없습니다")
            String id,

            @NotBlank(message = "PW는 공백이 될 수 없습니다")
            String password,

            @NotBlank(message = "휴대폰 번호는 공백이 될 수 없습니다.")
            @Size(min = 9, max = 11, message = "휴대폰 번호의 길이가 올바르지 않습니다.")
            @Pattern(regexp = "^[0-9]+$", message = "휴대폰 번호는 숫자만 포함해야 합니다.")
            String phoneNumber
    ) {
    }

    public record Certification(
            @NotBlank(message = "휴대폰 번호는 공백이 될 수 없습니다.")
            @Size(min = 9, max = 11, message = "휴대폰 번호의 길이가 올바르지 않습니다.")
            @Pattern(regexp = "^[0-9]+$", message = "휴대폰 번호는 숫자만 포함해야 합니다.")
            String phoneNumber,

            @NotBlank(message = "인증번호는 공백이 될 수 없습니다.")
            @Pattern(regexp = "\\d{6}", message = "인증번호는 숫자 6자리여야 합니다.")
            String code
    ) {
        public static Certification of(String phoneNumber, String code) {
            return new Certification(phoneNumber, code);
        }
    }
}
