package com.jpmorgan.log.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LogSchedulerApplication {
    Logger logger = LoggerFactory.getLogger(LogSchedulerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LogSchedulerApplication.class, args);
    }

}
