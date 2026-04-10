package com.bms.bookmyseat.impl;

import com.bms.bookmyseat.dto.BookingRequest;
import com.bms.bookmyseat.dto.BookingResponse;
import com.bms.bookmyseat.entity.Booking;
import com.bms.bookmyseat.entity.Seat;
import com.bms.bookmyseat.entity.Show;
import com.bms.bookmyseat.exception.BookingNotFoundException;
import com.bms.bookmyseat.exception.InvalidBookingException;
import com.bms.bookmyseat.exception.SeatNotLockedException;
import com.bms.bookmyseat.repository.BookingRepository;
import com.bms.bookmyseat.repository.SeatRepository;
import com.bms.bookmyseat.repository.ShowRepository;
import com.bms.bookmyseat.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {

        log.info("Booking request userId={} showId={}", request.getUserId(), request.getShowId());

        if (request.getSeatIds() == null || request.getSeatIds().isEmpty()) {
            throw new InvalidBookingException("Seat list cannot be empty");
        }

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new InvalidBookingException("Show not found"));

        // 🔥 CRITICAL: DB LEVEL LOCK
        List<Seat> seats = seatRepository.findSeatsForUpdate(request.getSeatIds());

        if (seats.size() != request.getSeatIds().size()) {
            throw new InvalidBookingException("Some seats not found");
        }

        double total = 0;

        for (Seat seat : seats) {

            log.info("Validating seat id={} status={}", seat.getId(), seat.getStatus());

            if (!seat.getShow().getId().equals(show.getId())) {
                throw new InvalidBookingException("Seat does not belong to this show");
            }

            if (seat.getStatus() != Seat.SeatStatus.LOCKED) {
                log.warn("Seat not locked id={}", seat.getId());
                throw new SeatNotLockedException("Seat already booked by another user");
            }

            total += seat.getPrice();
        }

        // 🔥 UPDATE STATUS
        seats.forEach(seat -> seat.setStatus(Seat.SeatStatus.BOOKED));
        seatRepository.saveAll(seats);

        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .show(show)
                .seats(seats)
                .totalAmount(total)
                .status(Booking.BookingStatus.CONFIRMED)
                .build();

        bookingRepository.save(booking);

        log.info("Booking successful id={} amount={}", booking.getId(), total);

        return map(booking);
    }

    @Override
    public BookingResponse getBooking(Long id) {

        log.info("Fetching booking id={}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        return map(booking);
    }

    @Override
    public Page<BookingResponse> getUserBookings(Long userId, int page, int size) {

        log.info("Fetching bookings for userId={}", userId);

        Pageable pageable = PageRequest.of(page, size);

        return bookingRepository.findByUserId(userId, pageable)
                .map(this::map);
    }

    @Override
    @Transactional
    public void cancelBooking(Long id) {

        log.info("Cancelling booking id={}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new InvalidBookingException("Booking already cancelled");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);

        // 🔥 OPTIONAL: lock before update (extra safety)
        List<Seat> seats = seatRepository.findSeatsForUpdate(
                booking.getSeats().stream().map(Seat::getId).toList()
        );

        seats.forEach(seat -> seat.setStatus(Seat.SeatStatus.AVAILABLE));

        seatRepository.saveAll(seats);
        bookingRepository.save(booking);

        log.info("Booking cancelled successfully id={}", id);
    }

    private BookingResponse map(Booking booking) {

        return BookingResponse.builder()
                .bookingId(booking.getId())
                .showId(booking.getShow().getId())
                .seatIds(
                        booking.getSeats().stream()
                                .map(Seat::getId)
                                .toList()
                )
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus().name())
                .build();
    }
}