package com.shinhan.pda_midterm_project.domain.news.service;

import com.shinhan.pda_midterm_project.common.util.SseEmitterRepository;
import com.shinhan.pda_midterm_project.domain.news.repository.NewsRepository;
import com.shinhan.pda_midterm_project.domain.notification.model.Notification;
import com.shinhan.pda_midterm_project.domain.notification.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

}
