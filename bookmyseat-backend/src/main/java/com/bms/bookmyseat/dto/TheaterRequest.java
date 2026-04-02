package com.bms.bookmyseat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TheaterRequest {

    @NotBlank(message = "Theater name required")
    private String name;

    @NotBlank(message = "Location required")
    private String location;
}
