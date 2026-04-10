package com.bms.bookmyseat.impl;

import com.bms.bookmyseat.dto.*;
import com.bms.bookmyseat.dto.AuthResponse;

import com.bms.bookmyseat.entity.*;
import com.bms.bookmyseat.exception.*;
import com.bms.bookmyseat.repository.UserRepository;
import com.bms.bookmyseat.security.JwtUtil;
import com.bms.bookmyseat.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Registration failed - Email already exists: {}", request.getEmail());
            throw new UserAlreadyExistsException("Email already exists");
        }

                 User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        log.info("Registration successful {}", request.getEmail());

        return AuthResponse.builder()
                .token(jwtUtil.generateToken(user.getEmail(), user.getRole().name()))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed - User not found: {}", request.getEmail());
                    return new InvalidCredentialsException("Invalid email");
                });
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed - Invalid password for email: {}", request.getEmail());
            throw new InvalidCredentialsException("Invalid password");
        }
        log.info("User logged in successfully: {}", user.getEmail());
        return AuthResponse.builder()
                .token(jwtUtil.generateToken(user.getEmail(), user.getRole().name()))
                .build();
    }
}
