package com.johnoct.projects.stockbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"me.ramswaroop.jbot", "com.johnoct.projects.stockbot"})
public class StockbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockbotApplication.class, args);
	}
}
