package com.shinhan.pda_midterm_project.presentation.member.controller;


import static com.shinhan.pda_midterm_project.common.response.ResponseMessages.GET_MY_PAGE_SUCCESS;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.member.service.MyPageService;
import com.shinhan.pda_midterm_project.presentation.member.dto.response.MyPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public Response<MyPageResponse> getMyPageInfo(@Auth Accessor accessor) {
        MyPageResponse response = myPageService.getMyPageInfo(accessor.memberId());
        return Response.success(
                GET_MY_PAGE_SUCCESS.getCode(),
                GET_MY_PAGE_SUCCESS.getMessage(),
                response
        );
    }
}
