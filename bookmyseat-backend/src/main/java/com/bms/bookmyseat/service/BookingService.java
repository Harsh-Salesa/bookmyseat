package com.bms.bookmyseat.service;

import com.bms.bookmyseat.dto.BookingRequest;
import com.bms.bookmyseat.dto.BookingResponse;
import org.springframework.data.domain.Page;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request);

    BookingResponse getBooking(Long id);

    Page<BookingResponse> getUserBookings(Long userId, int page, int size);

    void cancelBooking(Long id);
}
