package com.jobtracker.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jobtracker.entity.ERole;
import com.jobtracker.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Find role by name (ROLE_USER, ROLE_ADMIN)
     * JPA automatically creates: SELECT * FROM roles WHERE name = ?
     */
    Optional<Role> findByName(ERole name);
}
