package com.shinhan.pda_midterm_project.domain.total_summary.controller;

import com.shinhan.pda_midterm_project.domain.total_summary.service.TotalSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/total-summaries")
public class TotalController {

    private final TotalSummaryService totalSummaryService;

    @GetMapping("/debug/print-member-ids")
    public String printMemberIds() {
        totalSummaryService.printAllMemberIds();
        return "Printed all member IDs to log.";
    }
}
