package com.bms.bookmyseat.service;

import com.bms.bookmyseat.dto.*;
import com.bms.bookmyseat.dto.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}