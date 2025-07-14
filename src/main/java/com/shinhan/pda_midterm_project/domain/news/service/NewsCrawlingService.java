//package com.shinhan.pda_midterm_project.domain.news.service;
//
//import com.shinhan.pda_midterm_project.domain.news.model.FinnhubNews;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//@Service
//public class NewsCrawlingService {
//
//    private final String API_KEY = "d1po5e1r01qrc6gvcc2gd1po5e1r01qrc6gvcc30"; // 발급받은 API 키를 여기에 입력하세요.
//    private final String API_URL = "https://finnhub.io/api/v1";
//
//    @Async("crawlingExecutor")
//    public CompletableFuture<List<FinnhubNews>> getCompanyNews(String ticker) {
//        System.out.println(Thread.currentThread().getName() + "에서 [" + ticker + "] 뉴스 API 호출");
//        RestTemplate restTemplate = new RestTemplate();
//
//        // 1. 날짜 형식을 'YYYY-MM-DD'로 지정
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        // 2. '오늘'과 '어제' 날짜를 계산
//        LocalDate today = LocalDate.now();
//        LocalDate yesterday = today.minusDays(1); // 오늘로부터 하루 전
//
//        // 3. 계산된 날짜를 API가 요구하는 형식의 문자열로 변환
//        String fromDate = yesterday.format(formatter);
//        String toDate = today.format(formatter);
//
//        // 4. URL에 from과 to 파라미터를 추가하여 하루 동안의 뉴스를 요청
//        String requestUrl = String.format("%s/company-news?symbol=%s&from=%s&to=%s&token=%s",
//                API_URL, ticker, fromDate, toDate, API_KEY);
//
//        System.out.println("요청 URL: " + requestUrl); // 생성된 URL 확인용 로그
//
//        ResponseEntity<List<FinnhubNews>> response = restTemplate.exchange(
//                requestUrl,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<FinnhubNews>>() {}
//        );
//
//        return CompletableFuture.completedFuture(response.getBody());
//    }
//}