package com.shinhan.pda_midterm_project.domain.main_news.service;

import com.shinhan.pda_midterm_project.domain.main_news.model.MainNews;
import java.util.List;

public interface MainNewsService {
    List<MainNews> getCurrentMainNews();
}
