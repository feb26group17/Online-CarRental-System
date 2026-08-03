package com.carrental.crudservice.controller;

import com.carrental.crudservice.dto.PaymentRequest;
import com.carrental.crudservice.dto.PaymentResponse;
import com.carrental.crudservice.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<PaymentResponse>> getMyPaymentHistory(Authentication authentication) {
        Integer userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(paymentService.getMyPaymentHistory(userId));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponse> getPaymentByBooking(@PathVariable Integer bookingId,
                                                                Authentication authentication) {
        Integer userId = getUserIdFromAuth(authentication);
        String role = getRoleFromAuth(authentication);
        return ResponseEntity.ok(paymentService.getPaymentByBookingId(bookingId, userId, role));
    }

    @GetMapping("/vehicle/{vehicleId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<PaymentResponse>> getPaymentsForVehicle(@PathVariable Integer vehicleId,
                                                                        Authentication authentication) {
        Integer userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(paymentService.getPaymentsForVehicle(vehicleId, userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
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
