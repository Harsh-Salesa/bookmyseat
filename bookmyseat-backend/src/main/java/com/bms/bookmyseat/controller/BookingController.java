import com.bms.bookmyseat.dto.BookingRequest;
import com.bms.bookmyseat.dto.BookingResponse;
import com.bms.bookmyseat.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponse create(@Valid @RequestBody BookingRequest request) {
        log.info("API: create booking");
        return bookingService.createBooking(request);
    }

    @GetMapping("/{id}")
    public BookingResponse get(@PathVariable Long id) {
        return bookingService.getBooking(id);
    }

    @GetMapping
    public Page<BookingResponse> getUserBookings(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return bookingService.getUserBookings(userId, page, size);
    }

    @DeleteMapping("/{id}")
    public String cancel(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return "Booking cancelled";
    }
}