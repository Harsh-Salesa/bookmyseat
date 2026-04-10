package com.bms.bookmyseat;

import com.bms.bookmyseat.dto.BookingRequest;
import com.bms.bookmyseat.entity.Seat;
import com.bms.bookmyseat.entity.Show;
import com.bms.bookmyseat.repository.SeatRepository;
import com.bms.bookmyseat.repository.ShowRepository;
import com.bms.bookmyseat.service.BookingService;
import com.bms.bookmyseat.service.SeatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class BookingConcurrencyTest {

    @Autowired
    private SeatService seatService;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingService bookingService;
    @Autowired
    private ShowRepository showRepository;
    @Test
    void testConcurrentBooking() throws InterruptedException {

        // 🔥 GET REAL SHOW

        Show show = showRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No show found"));

        Long showId = show.getId();

        // 🔥 RESET SEATS
        List<Seat> seats = seatRepository.findByShowId(showId);
        seats.forEach(s -> s.setStatus(Seat.SeatStatus.AVAILABLE));
        seatRepository.saveAll(seats);

        // 🔥 PICK SEAT FROM SAME SHOW
        List<Long> seatIds = seats.stream()
                .limit(1)
                .map(Seat::getId)
                .toList();

        // 🔥 LOCK
        seatService.lockSeats(seatIds);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable user1 = () -> {
            try {
                bookingService.createBooking(
                        new BookingRequest(showId, seatIds, 1L)
                );
                System.out.println("User1 success");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("User1 failed");
            }
        };

        Runnable user2 = () -> {
            try {
                bookingService.createBooking(
                        new BookingRequest(showId, seatIds, 2L)
                );
                System.out.println("User2 success");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("User2 failed");
            }
        };

        executor.submit(user1);
        executor.submit(user2);

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }}
