package com.fun.bookMyShow.service;

import com.fun.bookMyShow.Model.User;
import com.fun.bookMyShow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthService {

    private static final int TOKEN_BYTES = 32;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.reset-password.base-url:http://localhost:5173/reset-password}")
    private String resetPasswordBaseUrl;

    @Value("${app.reset-password.token-expiry-minutes:30}")
    private int resetTokenTtlMinutes;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return;
        }

        User user = userOptional.get();
        String token = generateToken();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(resetTokenTtlMinutes);

        user.setResetPasswordToken(token);
        user.setResetPasswordExpiry(expiry);
        user.setResetPasswordUsedAt(null);
        userRepository.save(user);

        String resetUrl = resetPasswordBaseUrl + "?token=" + token;
        emailService.sendPasswordResetMail(user, resetUrl);
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetPasswordToken(token);
        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();
        LocalDateTime now = LocalDateTime.now();

        if (user.getResetPasswordUsedAt() != null) {
            return false;
        }

        if (user.getResetPasswordExpiry() == null || user.getResetPasswordExpiry().isBefore(now)) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordUsedAt(now);
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiry(null);
        userRepository.save(user);
        return true;
    }

    private String generateToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
