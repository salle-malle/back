package com.shinhan.pda_midterm_project.presentation.member.controller;


import static com.shinhan.pda_midterm_project.common.response.ResponseMessages.GET_MY_PAGE_SUCCESS;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.member.service.MemberService;
import com.shinhan.pda_midterm_project.domain.member.service.MyPageService;
import com.shinhan.pda_midterm_project.presentation.member.dto.request.UpdateInvestmentTypeRequest;
import com.shinhan.pda_midterm_project.presentation.member.dto.request.UpdateNicknameRequest;
import com.shinhan.pda_midterm_project.presentation.member.dto.response.MyPageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final MemberService memberService;

    @GetMapping
    public Response<MyPageResponse> getMyPageInfo(@Auth Accessor accessor) {
        MyPageResponse response = myPageService.getMyPageInfo(accessor.memberId());
        return Response.success(
                GET_MY_PAGE_SUCCESS.getCode(),
                GET_MY_PAGE_SUCCESS.getMessage(),
                response
        );
    }

    @PatchMapping("/nickname")
    public Response<Void> updateNickname(@Auth Accessor accessor,
                                         @Valid @RequestBody UpdateNicknameRequest request) {
        memberService.updateNickname(accessor.memberId(), request.getNickname());
        return Response.success(
                ResponseMessages.UPDATE_NICKNAME_SUCCESS.getCode(),
                ResponseMessages.UPDATE_NICKNAME_SUCCESS.getMessage()
        );
    }

    @PatchMapping("/investment-type")
    public Response<Void> updateInvestmentType(
            @Auth Accessor accessor,
            @RequestBody @Valid UpdateInvestmentTypeRequest request
    ) {
        myPageService.updateInvestmentType(accessor.memberId(), request.investmentTypeId());
        return Response.success(
                ResponseMessages.UPDATE_INVESTMENT_TYPE_SUCCESS.getCode(),
                ResponseMessages.UPDATE_INVESTMENT_TYPE_SUCCESS.getMessage()
        );
    }

}
