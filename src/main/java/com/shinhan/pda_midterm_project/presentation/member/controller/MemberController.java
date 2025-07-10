package com.shinhan.pda_midterm_project.presentation.member.controller;

import static com.shinhan.pda_midterm_project.common.response.ResponseMessages.UPDATE_PHONE_NUMBER_SUCCESS;

import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.domain.member.service.MemberService;
import com.shinhan.pda_midterm_project.presentation.member.dto.request.MemberDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/api/v1/member"))
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/phone")
    public ResponseEntity<Response<String>> updatePhoneNumber(
            @Valid @RequestBody MemberDto.PhoneNumber phoneNumberRequest
    ) {
        Long memberId = 2L; // 테스트용 => 나중에 인증 후 뽑아옴
        memberService.updatePhoneNumber(memberId, phoneNumberRequest.phoneNumber());

        return ResponseEntity
                .ok()
                .body(Response.success(
                        UPDATE_PHONE_NUMBER_SUCCESS.getCode(),
                        UPDATE_PHONE_NUMBER_SUCCESS.getMessage()
                ));
    }
}