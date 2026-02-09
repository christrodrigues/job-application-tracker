package com.jobtracker.service;

import com.jobtracker.dto.AuthDTOs.*;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobtracker.entity.ERole;
import com.jobtracker.entity.Role;
import com.jobtracker.entity.User;
import com.jobtracker.repository.RoleRepository;
import com.jobtracker.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Register a new user
     * 
     * @param signupRequest contains username, email, password
     * @return success message
     * @throws RuntimeException if username or email already exists
     */

    @Transactional
    public String registerUser(SignupRequest signupRequest) {
        // Check if username already exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user account
        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()) // Encrypt password
        );

        // Assign default role (ROLE_USER)
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
        roles.add(userRole);

        user.setRoles(roles);

        // Save user to database
        userRepository.save(user);

        return "User registered successfully!";
    }

    /**
     * Check if username is available
     */
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    /**
     * Check if email is available
     */

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
}