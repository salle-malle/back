package com.shinhan.pda_midterm_project.test;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BatchTestRunner implements CommandLineRunner {
    private final JobLauncher jobLauncher;
    private final Job sampleJob;

    public BatchTestRunner(JobLauncher jobLauncher, Job sampleJob) {
        this.jobLauncher = jobLauncher;
        this.sampleJob = sampleJob;
    }

    @Override
    public void run(String... args) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(sampleJob, params);
    }
} 