package com.bms.bookmyseat.impl;

import com.bms.bookmyseat.dto.SeatResponse;
import com.bms.bookmyseat.entity.Seat;
import com.bms.bookmyseat.exception.SeatNotAvailableException;
import com.bms.bookmyseat.repository.SeatRepository;
import com.bms.bookmyseat.service.SeatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    @Override
    public List<SeatResponse> getSeatsByShow(Long showId) {

        log.info("Fetching seats for showId={}", showId);

        return seatRepository.findByShowId(showId)
                .stream()
                .map(this::map)
                .toList();
    }

    // 🔥 LOCK SEATS (CONCURRENCY SAFE)
    @Override
    @Transactional
    public void lockSeats(List<Long> seatIds) {

        log.info("Lock seats request received: {}", seatIds);

        // 🔥 IMPORTANT CHANGE: DB LEVEL LOCK
        List<Seat> seats = seatRepository.findSeatsForUpdate(seatIds);

        if (seats.size() != seatIds.size()) {
            throw new RuntimeException("Some seats not found");
        }

        for (Seat seat : seats) {

            log.info("Checking seat id={} status={}", seat.getId(), seat.getStatus());

            if (seat.getStatus() != Seat.SeatStatus.AVAILABLE) {
                log.warn("Seat already booked/locked: {}", seat.getId());
                throw new SeatNotAvailableException("Seat already booked or locked");
            }

            seat.setStatus(Seat.SeatStatus.LOCKED);
        }

        seatRepository.saveAll(seats);

        log.info("Seats locked successfully: {}", seatIds);
    }

    // 🔥 BOOK SEATS (SAFE)
    @Override
    @Transactional
    public void bookSeats(List<Long> seatIds) {

        log.info("Booking seats: {}", seatIds);

        // 🔥 IMPORTANT CHANGE: DB LOCK AGAIN
        List<Seat> seats = seatRepository.findSeatsForUpdate(seatIds);

        if (seats.size() != seatIds.size()) {
            throw new RuntimeException("Some seats not found");
        }

        for (Seat seat : seats) {

            log.info("Validating seat id={} status={}", seat.getId(), seat.getStatus());

            if (seat.getStatus() != Seat.SeatStatus.LOCKED) {
                log.warn("Seat not locked: {}", seat.getId());
                throw new RuntimeException("Seat not locked");
            }

            seat.setStatus(Seat.SeatStatus.BOOKED);
        }

        seatRepository.saveAll(seats);

        log.info("Seats booked successfully: {}", seatIds);
    }

    private SeatResponse map(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .type(seat.getType().name())
                .status(seat.getStatus().name())
                .build();
    }
}