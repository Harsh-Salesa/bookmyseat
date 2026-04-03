package com.bms.bookmyseat.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ShowRequest {

    @NotNull(message = "Movie ID required")
    private Long movieId;

    @NotNull(message = "Theater ID required")
    private Long theaterId;

    @NotNull(message = "Show date required")
    private LocalDate showDate;

    @NotNull(message = "Show time required")
    private LocalTime showTime;
}