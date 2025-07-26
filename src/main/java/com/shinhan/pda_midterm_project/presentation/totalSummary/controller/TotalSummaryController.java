package com.shinhan.pda_midterm_project.presentation.totalSummary.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.total_summary.service.TotalSummaryService;
import com.shinhan.pda_midterm_project.presentation.totalSummary.dto.TotalSummaryResponseDto;
import kotlin.reflect.jvm.internal.impl.renderer.ClassifierNamePolicy.SOURCE_CODE_QUALIFIED;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/total-summary")
@RequiredArgsConstructor
public class TotalSummaryController {
    private final TotalSummaryService totalSummaryService;

    @MemberOnly
    @GetMapping("/today-summary")
    public ResponseEntity<Response<TotalSummaryResponseDto>> getTodaySummary(@Auth Accessor accessor) {
        Long memberId = accessor.memberId();
        String todaySummary = totalSummaryService.getTodaySummary(memberId);
        TotalSummaryResponseDto totalSummaryResponse = TotalSummaryResponseDto.of(todaySummary);
        return ResponseEntity
                .ok()
                .body(
                        Response.success(
                                ResponseMessages.SUCCESS.getCode(),
                                ResponseMessages.SIGNUP_SUCCESS.getMessage(),
                                totalSummaryResponse
                        ));
    }
}
