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

        return seatRepository.findByShowId(showId)
                .stream()
                .map(this::map)
                .toList();
    }

    // 🔥 LOCK SEATS
    @Override
    @Transactional
    public void lockSeats(List<Long> seatIds) {

        log.info("Lock seats request received: {}", seatIds);
        List<Seat> seats = seatRepository.findAllById(seatIds);

        for (Seat seat : seats) {

                if (seat.getStatus() != Seat.SeatStatus.AVAILABLE) {
                    log.warn("Seat already booked/locked: {}", seat.getId());
                    throw new SeatNotAvailableException("Seat already booked or locked");
                }

                seat.setStatus(Seat.SeatStatus.LOCKED);
            }


        seatRepository.saveAll(seats);

        log.info("Seats locked: {}", seatIds);
    }

    // 🔥 BOOK SEATS
    @Override
    @Transactional
    public void bookSeats(List<Long> seatIds) {

        List<Seat> seats = seatRepository.findAllById(seatIds);

        for (Seat seat : seats) {

            if (seat.getStatus() != Seat.SeatStatus.LOCKED) {
                throw new RuntimeException("Seat not locked");
            }

            seat.setStatus(Seat.SeatStatus.BOOKED);
        }

        seatRepository.saveAll(seats);

        log.info("Seats booked: {}", seatIds);
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