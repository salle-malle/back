package com.shinhan.pda_midterm_project.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private final NotificationService notificationService;

//    @Scheduled(cron = "*/5 * * * * ?")
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendMorningNotifications() {
        log.info("아침 8시! 접속 중인 유저에게 알림 발송 시작");

        notificationService.sendBatchNotifications();

        log.info("알림 발송 완료");
    }
}
