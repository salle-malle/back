package com.shinhan.pda_midterm_project.presentation.disclosure.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureSimpleDto;
import com.shinhan.pda_midterm_project.domain.disclosure.service.DisclosureService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/disclosure")
@RequiredArgsConstructor
public class DisclosureController {
    private final DisclosureService disclosureService;

    @MemberOnly
    @GetMapping("/my-current-disclosure")
    public ResponseEntity<Response<List<DisclosureSimpleDto>>> getMyCurrentDisclosure(@Auth Accessor accessor) {
        Long memberId = accessor.memberId();
        List<DisclosureSimpleDto> myCurrentDisclosure = disclosureService.getMyCurrentDisclosure(memberId);

        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessages.GET_MY_CURRENT_DISCLOSURE_SUCCESS.getCode(),
                        ResponseMessages.GET_MY_CURRENT_DISCLOSURE_SUCCESS.getMessage(),
                        myCurrentDisclosure
                ));
    }
}
