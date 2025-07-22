package com.shinhan.pda_midterm_project.presentation.member.controller;

import static com.shinhan.pda_midterm_project.common.response.ResponseMessages.UPDATE_PHONE_NUMBER_SUCCESS;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.auth.service.SmsCertificationService;
import com.shinhan.pda_midterm_project.domain.member.service.MemberService;
import com.shinhan.pda_midterm_project.presentation.auth.dto.request.AuthRequest;
import com.shinhan.pda_midterm_project.presentation.member.dto.request.MemberDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final SmsCertificationService smsCertificationService;

    @MemberOnly
    @PatchMapping("/phone")
    public ResponseEntity<Response<String>> updatePhoneNumber(
            @Auth Accessor accessor,
            @Valid @RequestBody MemberDto.PhoneNumber phoneNumberRequest
    ) {
        Long memberId = accessor.memberId();
        memberService.updatePhoneNumber(memberId, phoneNumberRequest.phoneNumber());

        return ResponseEntity
                .ok()
                .body(Response.success(
                        UPDATE_PHONE_NUMBER_SUCCESS.getCode(),
                        UPDATE_PHONE_NUMBER_SUCCESS.getMessage()
                ));
    }

    @PostMapping("number/verify")
    public ResponseEntity<Response<String>> verifyCertificationNumber(
            @Valid @RequestBody AuthRequest.Certification certificationRequest
    ) {
        String code = certificationRequest.code();
        String phoneNumber = certificationRequest.phoneNumber();

        smsCertificationService.verifyNumber(code, phoneNumber);

        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessages.CERTIFICATION_VERIFY_SUCCESS.getCode(),
                        ResponseMessages.CERTIFICATION_VERIFY_SUCCESS.getMessage()
                ));
    }

    @PostMapping("number/request")
    public ResponseEntity<Response<String>> getCertificationNumber(
            @Valid @RequestBody MemberDto.PhoneNumber phoneNumberRequest
    ) {
        String phoneNumber = phoneNumberRequest.phoneNumber();
        smsCertificationService.issueCertificationSms(phoneNumber);

        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessages.CERTIFICATION_REQUEST_SUCCESS.getCode(),
                        ResponseMessages.CERTIFICATION_REQUEST_SUCCESS.getMessage()
                ));
    }

    @PostMapping("check/memberId")
    public ResponseEntity<Response<String>> checkMemberIdDuplicated(
            @Valid @RequestBody MemberDto.MemberId memberIdRequest
    ) {
        String memberId = memberIdRequest.memberId();
        memberService.checkMemberIdDuplicated(memberId);
        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessages.SUCCESS.getCode(),
                        ResponseMessages.SUCCESS.getMessage()
                ));
    }
}
