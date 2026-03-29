package com.bms.bookmyseat.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name required")
    private String name;

    @Email
    @NotBlank
    private String email;

    @Size(min = 4,max=15)
    private String password;
}