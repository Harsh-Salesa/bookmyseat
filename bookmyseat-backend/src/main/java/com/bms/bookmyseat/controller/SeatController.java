package com.bms.bookmyseat.controller;

import com.bms.bookmyseat.dto.SeatLockRequest;
import com.bms.bookmyseat.dto.SeatResponse;
import com.bms.bookmyseat.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
@Slf4j
public class SeatController {

    private final SeatService seatService;

    // 🎯 Get seat layout
    @GetMapping("/{showId}")
    public List<SeatResponse> getSeats(@PathVariable Long showId) {
        return seatService.getSeatsByShow(showId);
    }

    // 🔒 Lock seats
    @PostMapping("/lock")
    public String lockSeats(@Valid @RequestBody SeatLockRequest request) {
        seatService.lockSeats(request.getSeatIds());
        return "Seats locked";
    }

    // 💳 Book seats
    @PostMapping("/book")
    public String bookSeats(@RequestBody SeatLockRequest request) {
        seatService.bookSeats(request.getSeatIds());
        return "Seats booked";
    }
}
