package com.shinhan.pda_midterm_project.presentation.member.dto.response;

public record MemberResponse(
        String memberName,
        String memberNickname
) {
    public static MemberResponse of(String memberName, String memberNickname) {
        return new MemberResponse(memberName, memberNickname);
    }
}
