package com.shinhan.pda_midterm_project.common.config;

import com.shinhan.pda_midterm_project.domain.notification.model.Notification;
import com.shinhan.pda_midterm_project.domain.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job sampleJob(JobRepository jobRepository, Step sampleStep) {
        return new JobBuilder("sampleJob", jobRepository)
                .start(sampleStep)
                .build();
    }

    @Bean
    public Step sampleStep(JobRepository jobRepository,
                           PlatformTransactionManager transactionManager,
                           ListItemReader<Long> memberIdReader,
                           ItemProcessor<Long, List<Notification>> notificationProcessor,
                           ItemWriter<List<Notification>> notificationWriter) {

        return new StepBuilder("sampleStep", jobRepository)
                .<Long, List<Notification>>chunk(5, transactionManager) // 일단 테스트 수준에서 5명씩 처리
                .reader(memberIdReader)
                .processor(notificationProcessor)
                .writer(notificationWriter)
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<Long> memberIdReader(NotificationService notificationService) {
        List<Long> allIds = new ArrayList<>(notificationService.getConnectedUserIds());
        log.info(">>> [Reader] 접속 중인 유저 IDs: {}", allIds);
        return new ListItemReader<>(allIds);
    }

    @Bean
    public ItemProcessor<Long, List<Notification>> notificationProcessor(NotificationService notificationService) {
        return memberId -> {
            var notifications = notificationService.getTodaysNotifications(memberId);
            log.info(">>> [Processor] memberId: {}, 알림 개수: {}", memberId, notifications.size());
            return notifications;
        };
    }

    @Bean
    public ItemWriter<List<Notification>> notificationWriter(NotificationService notificationService) {
        return notificationLists -> {
            log.info(">>> [Writer] 이번 Chunk에 포함된 유저 수: {}", notificationLists.size());
            for (List<Notification> notifications : notificationLists) {
                if (!notifications.isEmpty()) {
                    Long memberId = notifications.get(0).getMember().getId();
                    for (Notification notification : notifications) {
                        notificationService.sendNotification(memberId, notification.getNotificationContent());
                        log.info(">>> [Writer] 알림 전송 완료 - memberId: {}", memberId);
                    }
                } else {
                    log.info(">>> [Writer] 알림 없음 - 건너뜀");
                }
            }
        };
    }
}
