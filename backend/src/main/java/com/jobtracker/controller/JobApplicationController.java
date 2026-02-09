package com.jobtracker.controller;

import com.jobtracker.dto.JobApplicationDTOs.*;
import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.JobApplication;
import com.jobtracker.entity.User;
import com.jobtracker.repository.UserRepository;
import com.jobtracker.security.UserDetailsImpl;
import com.jobtracker.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * JobApplicationController
 * Day 3: CRUD endpoints for job applications
 * 
 * All endpoints require authentication (JWT token)
 */
@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*", maxAge = 3600)
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private UserRepository userRepository;

    /**
     * GET /api/applications
     * Get all applications for the authenticated user
     * Supports pagination, sorting, filtering
     */
    @GetMapping
    public ResponseEntity<?> getAllApplications(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateApplied") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) String keyword) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page<JobApplication> applications;

        if (keyword != null && !keyword.isEmpty()) {
            // Search by keyword
            applications = jobApplicationService.searchApplications(
                    user, keyword, page, size, sortBy, direction);
        } else if (status != null) {
            // Filter by status
            applications = jobApplicationService.getApplicationsByStatus(
                    user, status, page, size, sortBy, direction);
        } else {
            // Get all applications
            applications = jobApplicationService.getAllApplications(
                    user, page, size, sortBy, direction);
        }

        // Convert to DTOs
        Page<JobApplicationResponse> response = applications.map(JobApplicationResponse::new);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/applications/{id}
     * Get a specific application by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getApplicationById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobApplication application = jobApplicationService.getApplicationById(id, user);

        return ResponseEntity.ok(new JobApplicationResponse(application));
    }

    /**
     * POST /api/applications
     * Create a new job application
     */
    @PostMapping
    public ResponseEntity<?> createApplication(
            @Valid @RequestBody CreateJobApplicationRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobApplication application = jobApplicationService.createApplication(request, user);

        return ResponseEntity.ok(new JobApplicationResponse(application));
    }

    /**
     * PUT /api/applications/{id}
     * Update an existing application
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody UpdateJobApplicationRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobApplication application = jobApplicationService.updateApplication(id, request, user);

        return ResponseEntity.ok(new JobApplicationResponse(application));
    }

    /**
     * DELETE /api/applications/{id}
     * Delete (soft delete) an application
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        jobApplicationService.deleteApplication(id, user);

        return ResponseEntity.ok(Map.of("message", "Application deleted successfully"));
    }

    /**
     * GET /api/applications/stats
     * Get statistics (count by status)
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getStatistics(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Long> stats = jobApplicationService.getStatistics(user);

        return ResponseEntity.ok(stats);
    }
}