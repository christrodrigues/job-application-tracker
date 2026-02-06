package com.jobtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobTrackerApplication.class, args);
        System.out.println("========================================");
        System.out.println("Job Tracker Application Started!");
        System.out.println("Access at: http://localhost:8080");
        System.out.println("========================================");
    }
}
