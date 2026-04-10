package com.bms.bookmyseat.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingRequest
 {

    @NotNull
    private Long showId;

    @NotEmpty
    private List<Long> seatIds;

    @NotNull
    private Long userId;
}
