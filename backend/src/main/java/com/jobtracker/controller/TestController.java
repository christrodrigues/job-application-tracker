package com.jobtracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Job Tracker API is running!");
        response.put("timestamp", LocalDateTime.now());
        response.put("day", "Day 1 - Foundation Complete!");

        return response;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Job Tracker API! Day 1 Setup Complete! 🚀";
    }
}