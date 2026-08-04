package com.carrental.bookingservice.controller;

import com.carrental.bookingservice.dto.RefundRequest;
import com.carrental.bookingservice.dto.RefundResponse;
import com.carrental.bookingservice.dto.StatusUpdateRequest;
import com.carrental.bookingservice.service.RefundService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<RefundResponse> requestRefund(@Valid @RequestBody RefundRequest request,
                                                        Authentication authentication) {
        Integer userId = getUserIdFromAuth(authentication);
        RefundResponse response = refundService.requestRefund(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RefundResponse>> getAllRefunds() {
        return ResponseEntity.ok(refundService.getAllRefunds());
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<RefundResponse>> getMyRefunds(Authentication authentication) {
        Integer userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(refundService.getMyRefunds(userId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RefundResponse> updateRefundStatus(@PathVariable Integer id,
                                                             @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(refundService.updateRefundStatus(id, request.getStatus()));
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
}
