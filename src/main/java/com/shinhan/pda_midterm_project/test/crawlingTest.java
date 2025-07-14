package com.shinhan.pda_midterm_project.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/api/v1/member"))
@RequiredArgsConstructor
public class crawlingTest {
    //미장 종목 한개 넣는다, 10초마다 한번 url 크롤링한다.
}
