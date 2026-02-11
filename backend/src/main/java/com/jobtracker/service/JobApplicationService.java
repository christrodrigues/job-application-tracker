package com.jobtracker.service;

import com.jobtracker.dto.JobApplicationDTOs.*;
import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.JobApplication;
import com.jobtracker.entity.User;
import com.jobtracker.exception.CustomExceptions;
import com.jobtracker.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * JobApplicationService
 * Day 2: Job Application Business Logic
 * 
 * All CRUD operations for job applications
 * Ensures users can only access their own data
 */
@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    /**
     * Get all applications for a user (with pagination)
     */
    public Page<JobApplication> getAllApplications(
            User user,
            int page,
            int size,
            String sortBy,
            String direction) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return jobApplicationRepository.findByUserAndDeletedFalse(user, pageable);
    }

    /**
     * Get applications filtered by status
     */
    public Page<JobApplication> getApplicationsByStatus(
            User user,
            ApplicationStatus status,
            int page,
            int size,
            String sortBy,
            String direction) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return jobApplicationRepository.findByUserAndStatusAndDeletedFalse(
                user, status, pageable);
    }

    /**
     * Search applications by keyword (company or role)
     */
    public Page<JobApplication> searchApplications(
            User user,
            String keyword,
            int page,
            int size,
            String sortBy,
            String direction) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return jobApplicationRepository.searchByKeyword(user, keyword, pageable);
    }

    /**
     * Get a single application by ID
     * Ensures user can only access their own application
     */
    public JobApplication getApplicationById(Long id, User user) {
        return jobApplicationRepository.findByIdAndUserAndDeletedFalse(id, user)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException(
                        "JobApplication", "id", id));
    }

    /**
     * Create a new job application
     */
    @Transactional
    public JobApplication createApplication(
            CreateJobApplicationRequest request,
            User user) {
        JobApplication application = new JobApplication();
        application.setCompany(request.getCompany());
        application.setRole(request.getRole());
        application.setStatus(request.getStatus());
        application.setDateApplied(request.getDateApplied());
        application.setNotes(request.getNotes());
        application.setUser(user);
        application.setDeleted(false);

        return jobApplicationRepository.save(application);
    }

    /**
     * Update an existing application
     */
    @Transactional
    public JobApplication updateApplication(
            Long id,
            UpdateJobApplicationRequest request,
            User user) {
        JobApplication application = getApplicationById(id, user);

        // Update fields
        if (request.getCompany() != null) {
            application.setCompany(request.getCompany());
        }
        if (request.getRole() != null) {
            application.setRole(request.getRole());
        }
        if (request.getStatus() != null) {
            application.setStatus(request.getStatus());
        }
        if (request.getDateApplied() != null) {
            application.setDateApplied(request.getDateApplied());
        }
        if (request.getNotes() != null) {
            application.setNotes(request.getNotes());
        }

        return jobApplicationRepository.save(application);
    }

    /**
     * Delete application (soft delete)
     */
    @Transactional
    public void deleteApplication(Long id, User user) {
        JobApplication application = getApplicationById(id, user);
        application.setDeleted(true);
        jobApplicationRepository.save(application);
    }

    /**
     * Get statistics (counts by status)
     */
    public Map<String, Long> getStatistics(User user) {
        Map<String, Long> stats = new HashMap<>();

        stats.put("total", jobApplicationRepository.countByUserAndDeletedFalse(user));
        stats.put("applied", jobApplicationRepository.countByUserAndStatusAndDeletedFalse(
                user, ApplicationStatus.APPLIED));
        stats.put("interview", jobApplicationRepository.countByUserAndStatusAndDeletedFalse(
                user, ApplicationStatus.INTERVIEW));
        stats.put("offer", jobApplicationRepository.countByUserAndStatusAndDeletedFalse(
                user, ApplicationStatus.OFFER));
        stats.put("rejected", jobApplicationRepository.countByUserAndStatusAndDeletedFalse(
                user, ApplicationStatus.REJECTED));

        return stats;
    }
}