package com.shinhan.pda_midterm_project.domain.member.model;

import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String memberId;

    @Column(length = 30)
    private String phoneNumber;

    @Column(length = 15)
    private String nickname;

    @Column(length = 5)
    private String name;

    public void updateProfile(String name, String nickname, String phoneNumber) {
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    // TODO: 나중에 erd 확정 후 추가 : 기본적인 예시용
    public static Member create(String memberId, String phoneNumber) {
        return Member.builder()
                .memberId(memberId)
                .phoneNumber(phoneNumber)
                .build();
    }
}
