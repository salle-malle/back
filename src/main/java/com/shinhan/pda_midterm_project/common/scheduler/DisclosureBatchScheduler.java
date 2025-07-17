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
    private final Job disclosureJob;

    @Scheduled(cron = "0 30 6 * * MON-FRI", zone = "Asia/Seoul")
    public void runDisclosureBatch() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(disclosureJob, jobParameters);
            log.info("2분마다 Disclosure Job 실행!");
        } catch (Exception e) {
            log.error("Disclosure 배치 실행 중 오류", e);
        }
    }
}
