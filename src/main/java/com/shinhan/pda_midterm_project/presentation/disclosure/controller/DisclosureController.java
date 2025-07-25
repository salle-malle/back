package com.shinhan.pda_midterm_project.presentation.disclosure.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.config.DisclosureInitBatch;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.common.util.TimeUtil;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.disclosure.DisclosureProcessor;
import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureSimpleDto;
import com.shinhan.pda_midterm_project.domain.disclosure.service.DisclosureService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/disclosure")
@RequiredArgsConstructor
public class DisclosureController {
    private final DisclosureService disclosureService;
    private final DisclosureInitBatch disclosureInitBatch;

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

    @GetMapping("/init")
    public ResponseEntity<Response<List<String>>> disclosureInit() {
        disclosureInitBatch.runDisclosureBatch();

        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessages.SUCCESS.getCode(),
                        ResponseMessages.SUCCESS.getMessage()
                ));
    }

    @GetMapping("/{disclosureId}")
    @MemberOnly
    public ResponseEntity<Response<DisclosureSimpleDto>> getDisclosureDetail(
            @PathVariable Long disclosureId,
            @Auth Accessor accessor
    ) {
        DisclosureSimpleDto monoDisclosure = disclosureService.getMonoDisclosure(disclosureId);
        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessages.GET_DISCLOSURE_DETAIL_SUCCESS.getCode(),
                        ResponseMessages.GET_DISCLOSURE_DETAIL_SUCCESS.getMessage(),
                        monoDisclosure
                ));
    }

    @GetMapping("/my-disclosures")
    @MemberOnly
    public ResponseEntity<Response<List<DisclosureSimpleDto>>> getMyDisclosures(
            @Auth Accessor accessor
    ) {
        Long memberId = accessor.memberId();
        List<DisclosureSimpleDto> disclosures = disclosureService.getMyDisclosures(memberId);

        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessages.GET_DISCLOSURES_SUCCESS.getCode(),
                        ResponseMessages.GET_DISCLOSURES_SUCCESS.getMessage(),
                        disclosures
                ));
    }
}
