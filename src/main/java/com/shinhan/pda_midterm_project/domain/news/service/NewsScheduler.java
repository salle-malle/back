package com.shinhan.pda_midterm_project.domain.news.service;

import com.shinhan.pda_midterm_project.domain.summary.service.SummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsScheduler {
    private final JobLauncher jobLauncher;
    private final Job newsCrawlingJob;
    private final SummaryService summaryService;

    // 매일 오전 7시에 실행 (cron = "0 0 7 * * *")
    // 테스트를 위해 1분마다 실행하려면 "0 */1 * * * ?"
    @Scheduled(cron = "0 */5 * * * ?")
    public void runNewsCrawlingJob() {
        log.info("뉴스 크롤링 배치 작업을 시작합니다.");

        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();
            jobLauncher.run(newsCrawlingJob, params);
            log.info("뉴스 크롤링 배치 작업을 성공적으로 실행 완료했습니다.");

            summaryService.generateSummaryForTodayNews();
            log.info(">>> [Summary] 오늘 뉴스에 대한 요약도 성공적으로 생성 완료했습니다.");
        } catch (Exception e) {
            log.error("뉴스 크롤링 배치 작업 실행 중 오류 발생", e);
        }
    }
}