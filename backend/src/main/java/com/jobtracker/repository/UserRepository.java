package com.jobtracker.repository;

import com.jobtracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find user by username
     * JPA automatically creates: SELECT * FROM users WHERE username = ?
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     * JPA automatically creates: SELECT * FROM users WHERE email = ?
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists by username
     * JPA automatically creates: SELECT COUNT(*) > 0 FROM users WHERE username = ?
     */
    Boolean existsByUsername(String username);

    /**
     * Check if a user exists by email
     * JPA automatically creates: SELECT COUNT(*) > 0 FROM users WHERE email = ?
     */
    Boolean existsByEmail(String email);

}