package com.shinhan.pda_midterm_project.domain.disclosure;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job sampleJob;

    @Scheduled(cron = "0 0 * * * ?")
    public void executeBatchJob() throws Exception {
        JobParameters parameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(sampleJob, parameters);
    }
}
