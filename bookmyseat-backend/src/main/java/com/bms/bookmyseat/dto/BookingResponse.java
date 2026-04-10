package com.bms.bookmyseat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class BookingResponse {

    private Long bookingId;
    private Long showId;
    private List<Long> seatIds;
    private Double totalAmount;
    private String status;
}
