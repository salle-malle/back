package com.shinhan.pda_midterm_project.domain.member.model;

import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import com.shinhan.pda_midterm_project.domain.investment_type.model.InvestmentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investment_type_id")
    private InvestmentType investmentType;

    @Column(nullable = false, length = 255, unique = true)
    private String memberId;

    @Column(columnDefinition = "TEXT")
    private String memberPassword;

    @Column(length = 255)
    private String memberNickname;

    @Column(length = 20)
    private String memberPhone;

    @Column(columnDefinition = "TEXT")
    private String memberProfileImg;

    @Column(columnDefinition = "TEXT")
    private String memberAppKey;

    @Column(columnDefinition = "TEXT")
    private String memberAppSecret;

    @Column(length = 255)
    private String memberAccountNumber;

    @Column
    private java.time.LocalDate memberCreatedAt;

    @Column
    private Boolean memberIsRead;

    public void updateProfile(String memberNickname, String memberPhone) {
        this.memberNickname = memberNickname;
        this.memberPhone = memberPhone;
    }

    // TODO: 나중에 erd 확정 후 추가 : 기본적인 예시용
    public static Member create(String memberId, String memberPassword, String memberNickname, String memberPhone) {
        return Member.builder()
                .memberId(memberId)
                .memberPassword(memberPassword)
                .memberNickname(memberNickname)
                .memberPhone(memberPhone)
                .build();
    }
}
