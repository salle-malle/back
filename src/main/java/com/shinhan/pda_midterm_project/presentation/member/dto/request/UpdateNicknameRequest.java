package com.shinhan.pda_midterm_project.presentation.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateNicknameRequest {
    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    private String nickname;
}
