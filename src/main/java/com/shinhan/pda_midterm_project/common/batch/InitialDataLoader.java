package com.shinhan.pda_midterm_project.common.batch;

import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import com.shinhan.pda_midterm_project.domain.notification.model.Notification;
import com.shinhan.pda_midterm_project.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public void run(String... args) throws Exception {
        // ⚡ 100명 Member 생성 + 각 Member 알림 생성
//        for (int i = 1; i <= 100; i++) {
//            Member member = memberRepository.save(Member.builder()
//                    .memberId("user" + i)
//                    .memberPassword("pw" + i)
//                    .memberPhone("010-0000-" + String.format("%04d", i))
//                    .build());
//
//            notificationRepository.save(Notification.builder()
//                    .member(member)
//                    .notificationTitle("알림 타이틀 " + i)
//                    .notificationContent("이것은 " + i + "번째 유저의 테스트 알림입니다.")
//                    .notificationIsRead(false)
//                    .notificationUrl("/sample/url/" + i)
//                    .build());
//        }
    }
}
