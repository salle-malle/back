//package com.shinhan.pda_midterm_project.domain.news.service;
//
//import com.shinhan.pda_midterm_project.domain.news.model.FinnhubNews;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.CompletableFuture;
//import java.util.stream.Collectors;
//
//@Component
//public class NewsFetcher implements CommandLineRunner {
//
//    private final NewsCrawlingService finnhubService;
//
//    public NewsFetcher(NewsCrawlingService finnhubService) {
//        this.finnhubService = finnhubService;
//    }
//
//    // CommandLineRunner: 애플리케이션 실행 시 이 코드를 자동으로 실행
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("\n🚀 지정된 티커 목록에 대한 뉴스 크롤링을 시작합니다...");
//
////        List<String> targetTickers = List.of("AAPL", "MSFT", "GOOGL", "TSLA", "NVDA");
//        List<String> targetTickers = List.of("AAPL");
//        System.out.println("크롤링 대상 티커: " + targetTickers);
//
//        // 1. 각 티커에 대해 병렬로 뉴스 API 호출
//        Map<String, CompletableFuture<List<FinnhubNews>>> futuresMap = targetTickers.stream()
//                .collect(Collectors.toMap(
//                        ticker -> ticker, // Map의 Key는 ticker 이름
//                        finnhubService::getCompanyNews // Map의 Value는 API 호출 결과 (Future)
//                ));
//
//        // 2. 모든 병렬 작업이 완료될 때까지 대기
//        CompletableFuture.allOf(futuresMap.values().toArray(new CompletableFuture[0])).join();
//        System.out.println("✅ 모든 뉴스 API 호출 완료!");
//
//        // 3. 결과를 티커별로 정리하여 출력
//        System.out.println("\n--- 📈 티커별 하루동안 최신 뉴스---");
//        futuresMap.forEach((ticker, newsFuture) -> {
//            try {
//                List<FinnhubNews> newsList = newsFuture.get(); // Future에서 결과 가져오기
//                System.out.println("\n▶ 종목: " + ticker);
//                if (newsList != null && !newsList.isEmpty()) {
//                    newsList.forEach(news -> {
//                        System.out.println("  --------------------");
//                        System.out.println("  ▶ 제목: " + news.getHeadline());
//                        System.out.println("    요약: " + news.getSummary());
//                        System.out.println("    출처: " + news.getSource());
//                        System.out.println("    URL: " + news.getUrl());
//                        System.out.println("    카테고리: " + news.getCategory());
//                        System.out.println("    시간: " + news.getDatetime());
//                        System.out.println("    Related: " + news.getRelated());
//
//                    });
//                } else {
//                    System.out.println("  - 이 종목에 대한 뉴스를 가져오지 못했습니다.");
//                }
//            } catch (Exception e) {
//                System.err.println("[" + ticker + "] 뉴스 결과를 가져오는 중 오류 발생: " + e.getMessage());
//            }
//        });
//        System.out.println("--------------------------------");
//    }
//}