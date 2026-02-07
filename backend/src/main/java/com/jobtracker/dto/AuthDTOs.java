package com.jobtracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AuthDTOs
 * Day 2: Data Transfer Objects for Authentication
 * 
 * Contains all request and response classes for auth operations
 */
public class AuthDTOs {

    /**
     * SignupRequest - Data needed to register a new user
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupRequest {
        
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        private String username;

        @NotBlank(message = "Email is required")
        @Size(max = 50, message = "Email must not exceed 50 characters")
        @Email(message = "Email should be valid")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
        private String password;
    }

    /**
     * LoginRequest - Data needed to login
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;
    }

    /**
     * JwtResponse - Response sent after successful login
     * Contains JWT token and user information
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JwtResponse {
        
        private String token;
        private String type = "Bearer";
        private Long id;
        private String username;
        private String email;
        private List<String> roles;

        public JwtResponse(String token, Long id, String username, String email, List<String> roles) {
            this.token = token;
            this.id = id;
            this.username = username;
            this.email = email;
            this.roles = roles;
        }
    }

    /**
     * MessageResponse - Simple message response
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageResponse {
        private String message;
    }
}
