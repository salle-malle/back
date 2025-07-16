package com.shinhan.pda_midterm_project.common.config;

import com.shinhan.pda_midterm_project.domain.disclosure.DisclosureProcessor;
import com.shinhan.pda_midterm_project.domain.disclosure.DisclosureReader;
import com.shinhan.pda_midterm_project.domain.disclosure.DisclosureWriter;
import com.shinhan.pda_midterm_project.domain.disclosure.model.Disclosure;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class DisclosureBatchConfig {

    private final DisclosureReader disclosureReader;
    private final DisclosureProcessor disclosureProcessor;
    private final DisclosureWriter disclosureWriter;

    @Bean
    public Job disclosureJob(JobRepository jobRepository, Step disclosureChunkStep) {
        return new JobBuilder("disclosureJob", jobRepository)
                .start(disclosureChunkStep)
                .build();
    }

    @Bean
    public Step disclosureChunkStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("disclosureChunkStep", jobRepository)
                .<Stock, Disclosure>chunk(100, transactionManager)
                .reader(disclosureReader)
                .processor(disclosureProcessor)
                .writer(disclosureWriter)
                .build();
    }
}
