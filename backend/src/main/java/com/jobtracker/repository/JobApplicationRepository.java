package com.jobtracker.repository;

import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.JobApplication;
import com.jobtracker.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    /**
     * Find all applications for a user (excluding soft-deleted)
     * With pagination support
     */
    Page<JobApplication> findByUserAndDeletedFalse(User user, Pageable pageable);

    /**
     * Find applications by user and status
     * Used for filtering by status (APPLIED, INTERVIEW, OFFER, REJECTED)
     */
    Page<JobApplication> findByUserAndStatusAndDeletedFalse(
            User user,
            ApplicationStatus status,
            Pageable pageable);

    /**
     * Find a specific application by ID and user
     * Ensures users can only access their own applications
     */
    Optional<JobApplication> findByIdAndUserAndDeletedFalse(Long id, User user);

    /**
     * Search applications by company or role name
     * Custom JPQL query for flexible searching
     */
    @Query("SELECT ja FROM JobApplication ja WHERE ja.user = :user " +
            "AND ja.deleted = false " +
            "AND (LOWER(ja.company) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(ja.role) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<JobApplication> searchByKeyword(
            @Param("user") User user,
            @Param("keyword") String keyword,
            Pageable pageable);

    /**
     * Count applications by status for a user
     * Used for statistics dashboard
     */
    long countByUserAndStatusAndDeletedFalse(User user, ApplicationStatus status);

    /**
     * Count total applications for a user
     */
    long countByUserAndDeletedFalse(User user);

    /**
     * Find all applications for a user (for admin purposes - future feature)
     */
    List<JobApplication> findAllByUserAndDeletedFalse(User user);
}