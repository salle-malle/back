package com.shinhan.pda_midterm_project.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TestController {

  @GetMapping("/test")
  public String test() {
    return "서버 정상 동작 중!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
  }
}
