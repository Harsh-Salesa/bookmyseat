package com.bms.bookmyseat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SeatLockRequest {
    @NotEmpty(message = "Seat IDs cannot be empty")
    private List<Long> seatIds;
}
