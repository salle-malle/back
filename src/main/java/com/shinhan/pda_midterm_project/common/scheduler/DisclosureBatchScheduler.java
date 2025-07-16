package com.shinhan.pda_midterm_project.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DisclosureBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job disclosureJob; // BatchConfig 등에서 @Bean으로 등록한 Job과 이름 일치해야 함

    // 2분에 한 번 실행: "0 */2 * * * *"
    @Scheduled(cron = "0 */2 * * * *", zone = "Asia/Seoul")
    public void runDisclosureBatch() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()) // 중복인터벌 방지(항상 새로운 파라미터)
                    .toJobParameters();
            jobLauncher.run(disclosureJob, jobParameters);
            log.info("2분마다 Disclosure Job 실행!");
        } catch (Exception e) {
            log.error("Disclosure 배치 실행 중 오류", e);
        }
    }
}
