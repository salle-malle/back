package com.shinhan.pda_midterm_project.presentation.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MemberDto {
    public record PhoneNumber(
            @NotBlank(message = "휴대폰 번호는 공백이 될 수 없습니다.")
            @Size(min = 8, max = 11, message = "휴대폰 번호의 길이가 올바르지 않습니다.")
            @Pattern(regexp = "^[0-9]+$", message = "휴대폰 번호는 숫자만 포함해야 합니다.")
            String phoneNumber
    ) {
        public static PhoneNumber of(String phoneNumber) {
            return new PhoneNumber(phoneNumber);
        }
    }
}
