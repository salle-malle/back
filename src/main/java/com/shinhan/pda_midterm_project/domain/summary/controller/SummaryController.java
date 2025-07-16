package com.shinhan.pda_midterm_project.domain.summary.controller;

import com.shinhan.pda_midterm_project.domain.summary.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/summary")
public class SummaryController {

    private final SummaryService summaryService;

    @PostMapping
    public String summarize(@RequestBody String content) {
        return summaryService.summarize(content);
    }
}
