package com.bms.bookmyseat.impl;

import com.bms.bookmyseat.dto.*;
import com.bms.bookmyseat.dto.AuthResponse;

import com.bms.bookmyseat.entity.*;
import com.bms.bookmyseat.exception.*;
import com.bms.bookmyseat.repository.UserRepository;
import com.bms.bookmyseat.security.JwtUtil;
import com.bms.bookmyseat.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

                 User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtUtil.generateToken(user.getEmail()))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email"));
        System.out.println("RAW: " + request.getPassword());
        System.out.println("HASH: " + user.getPassword());
        System.out.println("MATCH: " + passwordEncoder.matches(request.getPassword(), user.getPassword()));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        return AuthResponse.builder()
                .token(jwtUtil.generateToken(user.getEmail()))
                .build();
    }
}
