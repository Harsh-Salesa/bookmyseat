package com.bms.bookmyseat.service;

import com.bms.bookmyseat.dto.SeatResponse;

import java.util.List;

public interface SeatService {

    List<SeatResponse> getSeatsByShow(Long showId);

    void lockSeats(List<Long> seatIds);

    void bookSeats(List<Long> seatIds);
}
