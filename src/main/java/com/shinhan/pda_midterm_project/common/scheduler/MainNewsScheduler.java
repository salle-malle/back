package com.shinhan.pda_midterm_project.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class MainNewsScheduler {
    private final JobLauncher jobLauncher;
    private final Job mainNewsCrawlingJob;

    @Scheduled(cron = "0 0 6 * * *")
    public void runHeadlineNewsCrawlingJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();
            jobLauncher.run(mainNewsCrawlingJob, params);
        } catch (Exception e) {
            log.error("뉴스 크롤링 배치 작업 실행 중 오류 발생", e);
        }
    }
}
