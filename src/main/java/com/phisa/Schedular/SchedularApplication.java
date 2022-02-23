package com.phisa.Schedular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@SpringBootApplication
public class SchedularApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedularApplication.class, args);
		ScheduledConfiguration config = new ScheduledConfiguration();
		config.job();
	}


}
