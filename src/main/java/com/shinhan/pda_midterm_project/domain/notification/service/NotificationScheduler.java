package com.shinhan.pda_midterm_project.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private final NotificationService notificationService;
    private final JobLauncher jobLauncher;
    private final Job notificationSendJob;

//    @Scheduled(cron = "0 0 8 * * ?")
//    @Scheduled(cron = "0 */1 * * * ?")
    public void sendMorningNotifications() {
        log.info("아침 8시! 접속 중인 유저에게 알림 발송 시작");

        // Spring Batch Job 실행
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(notificationSendJob, params);
            log.info("Spring Batch Job 실행 완료");
        } catch (Exception e) {
            log.error("Spring Batch Job 실행 중 오류", e);
        }

        log.info("알림 발송 완료");
    }
}
