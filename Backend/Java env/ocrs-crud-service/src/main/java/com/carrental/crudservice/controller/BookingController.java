package com.carrental.crudservice.controller;

import com.carrental.crudservice.dto.BookingRequest;
import com.carrental.crudservice.dto.BookingResponse;
import com.carrental.crudservice.dto.StatusUpdateRequest;
import com.carrental.crudservice.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request,
                                                         Authentication authentication) {
        Integer userId = getUserIdFromAuth(authentication);
        BookingResponse response = bookingService.createBooking(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication authentication) {
        Integer userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(bookingService.getMyBookings(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Integer id, Authentication authentication) {
        Integer userId = getUserIdFromAuth(authentication);
        String role = getRoleFromAuth(authentication);
        return ResponseEntity.ok(bookingService.getBookingById(id, userId, role));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    @GetMapping("/vehicle/{vehicleId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<BookingResponse>> getBookingsForVehicle(@PathVariable Integer vehicleId,
                                                                        Authentication authentication) {
        Integer userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(bookingService.getBookingsByVehicle(vehicleId, userId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<BookingResponse> updateBookingStatus(@PathVariable Integer id,
                                                                @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, request.getStatus()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @SuppressWarnings("unchecked")
    private Integer getUserIdFromAuth(Authentication authentication) {
        if (authentication != null && authentication.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            Object userIdObj = details.get("userId");
            if (userIdObj instanceof Integer) {
                return (Integer) userIdObj;
            }
        }
        throw new RuntimeException("User ID not found in authentication token");
    }

    @SuppressWarnings("unchecked")
    private String getRoleFromAuth(Authentication authentication) {
        if (authentication != null && authentication.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            Object roleObj = details.get("role");
            if (roleObj instanceof String) {
                return (String) roleObj;
            }
        }
        throw new RuntimeException("Role not found in authentication token");
    }
}
