package com.jobtracker.dto;

import com.jobtracker.entity.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * JobApplicationDTOs
 * Day 2: Data Transfer Objects for Job Applications
 * 
 * Separates internal entities from external API
 */
public class JobApplicationDTOs {

    /**
     * CreateJobApplicationRequest - Data needed to create a new application
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateJobApplicationRequest {

        @NotBlank(message = "Company name is required")
        @Size(max = 100, message = "Company name must not exceed 100 characters")
        private String company;

        @NotBlank(message = "Role is required")
        @Size(max = 100, message = "Role must not exceed 100 characters")
        private String role;

        @NotNull(message = "Status is required")
        private ApplicationStatus status;

        @NotNull(message = "Date applied is required")
        private LocalDate dateApplied;

        @Size(max = 1000, message = "Notes must not exceed 1000 characters")
        private String notes;
    }

    /**
     * UpdateJobApplicationRequest - Data for updating an application
     * All fields are optional - only update what's provided
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateJobApplicationRequest {

        @Size(max = 100, message = "Company name must not exceed 100 characters")
        private String company;

        @Size(max = 100, message = "Role must not exceed 100 characters")
        private String role;

        private ApplicationStatus status;

        private LocalDate dateApplied;

        @Size(max = 1000, message = "Notes must not exceed 1000 characters")
        private String notes;
    }

    /**
     * JobApplicationResponse - Data sent to client
     * Includes all application details
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobApplicationResponse {

        private Long id;
        private String company;
        private String role;
        private ApplicationStatus status;
        private LocalDate dateApplied;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long userId;
        private String username;

        // Constructor from Entity
        public JobApplicationResponse(com.jobtracker.entity.JobApplication application) {
            this.id = application.getId();
            this.company = application.getCompany();
            this.role = application.getRole();
            this.status = application.getStatus();
            this.dateApplied = application.getDateApplied();
            this.notes = application.getNotes();
            this.createdAt = application.getCreatedAt();
            this.updatedAt = application.getUpdatedAt();
            this.userId = application.getUser().getId();
            this.username = application.getUser().getUsername();
        }
    }
}