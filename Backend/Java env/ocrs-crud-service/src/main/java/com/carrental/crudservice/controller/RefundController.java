package com.carrental.crudservice.controller;

import com.carrental.crudservice.dto.RefundRequest;
import com.carrental.crudservice.dto.RefundResponse;
import com.carrental.crudservice.service.RefundService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<RefundResponse> requestRefund(@Valid @RequestBody RefundRequest request) {
        RefundResponse response = refundService.requestRefund(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<RefundResponse> getRefundByPayment(@PathVariable Integer paymentId) {
        return ResponseEntity.ok(refundService.getRefundByPaymentId(paymentId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RefundResponse>> getAllRefunds() {
        return ResponseEntity.ok(refundService.getAllRefunds());
    }
}