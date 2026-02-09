package com.jobtracker.controller;

import com.jobtracker.dto.AuthDTOs.*;
import com.jobtracker.security.JwtUtils;
import com.jobtracker.security.UserDetailsImpl;
import com.jobtracker.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AuthController
 * Day 3: Authentication endpoints
 * 
 * Public endpoints for login and signup
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * POST /api/auth/login
     * Authenticate user and return JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Authenticate username and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Get user details
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Return JWT response
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    /**
     * POST /api/auth/signup
     * Register new user account
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            String message = authService.registerUser(signUpRequest);
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * GET /api/auth/test
     * Test endpoint to verify auth routes work
     */
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(new MessageResponse("Auth endpoints are working!"));
    }
}