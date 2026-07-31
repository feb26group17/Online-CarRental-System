package com.example.projectservicetwo.controller;

import com.example.projectservicetwo.dto.BookingResponseDTO;
import com.example.projectservicetwo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // GET /bookings
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // GET /bookings/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    // PUT /bookings/{id}/cancel
    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    // DELETE /bookings/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Booking with ID " + id + " has been deleted successfully.");
    }
}