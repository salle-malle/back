package com.shinhan.pda_midterm_project.domain.main_news.service;

import com.shinhan.pda_midterm_project.domain.main_news.model.MainNews;
import com.shinhan.pda_midterm_project.domain.main_news.repository.MainNewsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainNewsServiceImpl implements MainNewsService {
    private final MainNewsRepository mainNewsRepository;

    public List<MainNews> getCurrentMainNews() {
        Pageable pageable = PageRequest.of(0, 5);
        return mainNewsRepository.findAllByOrderByIdDesc(pageable);
    }
}
