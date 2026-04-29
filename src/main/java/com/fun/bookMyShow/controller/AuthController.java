package com.fun.bookMyShow.controller;


import com.fun.bookMyShow.DTO.ForgotPasswordRequest;
import com.fun.bookMyShow.DTO.LoginRequest;
import com.fun.bookMyShow.DTO.ResetPasswordRequest;

import com.fun.bookMyShow.DTO.RegisterRequest;
import com.fun.bookMyShow.Model.User;
import com.fun.bookMyShow.repository.UserRepository;
import com.fun.bookMyShow.security.JwtUtility;
import com.fun.bookMyShow.service.AuthService;
import com.fun.bookMyShow.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@CrossOrigin(origins = "https://book-my-show-frontend-434s.vercel.app/")

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthService authService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtility jwtUtility,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService,
                          AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Validate input
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return createErrorResponse("Email is required", HttpStatus.BAD_REQUEST);
            }
            if (request.getPassword() == null || request.getPassword().length() < 2) {
                return createErrorResponse("Password must be at least 6 characters", HttpStatus.BAD_REQUEST);
            }

            // Check if email already exists
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return createErrorResponse("Email already exists", HttpStatus.BAD_REQUEST);
            }

            // Create new user
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPhoneNumber(request.getPhoneNumber());

            user.setRole(User.Role.USER); // Correct enum value

            // Save user
            User savedUser = userRepository.save(user);
            emailService.sendWelcomeMail(savedUser);

            // Generate JWT token
            String token = jwtUtility.generateToken(savedUser.getEmail());

            // Return response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", savedUser.getId());
            response.put("email", savedUser.getEmail());
            response.put("name", savedUser.getName());
            response.put("role", savedUser.getRole().name());
            response.put("message", "Registration successful");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return createErrorResponse("Registration failed: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request) {
        try {
            // Validate input
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return createErrorResponse("Email is required", HttpStatus.BAD_REQUEST);
            }
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return createErrorResponse("Name is required", HttpStatus.BAD_REQUEST);
            }
            if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
                return createErrorResponse("Phone number is required", HttpStatus.BAD_REQUEST);
            }
            if (request.getPassword() == null || request.getPassword().length() < 3) {
                return createErrorResponse("Password must be at least 3 characters", HttpStatus.BAD_REQUEST);
            }

            // Check if email already exists
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return createErrorResponse("Email already exists", HttpStatus.BAD_REQUEST);
            }

            // Create new ADMIN user
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(User.Role.ADMIN);  // SET AS ADMIN

            // Save user
            User savedUser = userRepository.save(user);
            emailService.sendWelcomeMail(savedUser);

            // Generate JWT token
            String token = jwtUtility.generateToken(savedUser.getEmail());

            // Return response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", savedUser.getId());
            response.put("email", savedUser.getEmail());
            response.put("name", savedUser.getName());
            response.put("role", savedUser.getRole().name());
            response.put("message", "Admin registration successful");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return createErrorResponse("Admin registration failed: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Validate input
            if (request.getEmail() == null || request.getPassword() == null) {
                return createErrorResponse("Email and password are required", HttpStatus.BAD_REQUEST);
            }

            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Get user details
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate JWT token
            String token = jwtUtility.generateToken(user.getEmail());

            // Return response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", user.getId());
            response.put("email", user.getEmail());
            response.put("name", user.getName());
            response.put("role", user.getRole().name());
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return createErrorResponse("Invalid email or password", HttpStatus.UNAUTHORIZED);
        } catch (AuthenticationException e) {
            return createErrorResponse("Authentication failed", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return createErrorResponse("Login failed: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return createValidationErrorResponse("Invalid or missing Authorization header");
            }

            String token = authHeader.substring(7);
            String email = jwtUtility.extractUsername(token);

            if (email != null && jwtUtility.isTokenValid(token, email)) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("userId", user.getId());
                response.put("email", user.getEmail());
                response.put("name", user.getName());
                response.put("role", user.getRole().name());

                return ResponseEntity.ok(response);
            }

            return createValidationErrorResponse("Invalid token");

        } catch (Exception e) {
            return createValidationErrorResponse("Token validation failed: " + e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return createErrorResponse("Email is required", HttpStatus.BAD_REQUEST);
        }

        authService.initiatePasswordReset(request.getEmail().trim());
        Map<String, String> response = new HashMap<>();
        response.put("message", "If this email exists, a reset link has been sent.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        if (request.getToken() == null || request.getToken().trim().isEmpty()) {
            return createErrorResponse("Token is required", HttpStatus.BAD_REQUEST);
        }
        if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
            return createErrorResponse("New password must be at least 6 characters", HttpStatus.BAD_REQUEST);
        }

        boolean reset = authService.resetPassword(request.getToken().trim(), request.getNewPassword());
        if (!reset) {
            return createErrorResponse("Invalid or expired reset token", HttpStatus.BAD_REQUEST);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset successful");
        return ResponseEntity.ok(response);
    }

    // Helper method to create error responses
    private ResponseEntity<?> createErrorResponse(String message, HttpStatus status) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return ResponseEntity.status(status).body(error);
    }

    // Helper method for validation endpoint errors
    private ResponseEntity<?> createValidationErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("valid", false);
        error.put("error", message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
