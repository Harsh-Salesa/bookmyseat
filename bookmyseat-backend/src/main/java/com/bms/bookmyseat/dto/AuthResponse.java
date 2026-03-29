package com.bms.bookmyseat.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import lombok.Builder;

@Data
@Builder
public class AuthResponse {
    private String token;
}