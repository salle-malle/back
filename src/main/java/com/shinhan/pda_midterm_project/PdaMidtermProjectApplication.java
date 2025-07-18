package com.shinhan.pda_midterm_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PdaMidtermProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdaMidtermProjectApplication.class, args);
	}

}
