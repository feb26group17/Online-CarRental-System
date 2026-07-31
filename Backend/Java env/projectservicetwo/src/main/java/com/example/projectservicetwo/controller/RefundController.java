package com.example.projectservicetwo.controller;

import com.example.projectservicetwo.dto.RefundResponseDTO;
import com.example.projectservicetwo.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/refunds")
public class RefundController {

    private final RefundService refundService;

    @Autowired
    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    // GET /refunds
    @GetMapping
    public ResponseEntity<List<RefundResponseDTO>> getAllRefunds() {
        return ResponseEntity.ok(refundService.getAllRefunds());
    }

    // GET /refunds/{id}
    @GetMapping("/{id}")
    public ResponseEntity<RefundResponseDTO> getRefundById(@PathVariable Integer id) {
        return ResponseEntity.ok(refundService.getRefundById(id));
    }

    // PUT /refunds/{id}/approve
    @PutMapping("/{id}/approve")
    public ResponseEntity<RefundResponseDTO> approveRefund(@PathVariable Integer id) {
        return ResponseEntity.ok(refundService.approveRefund(id));
    }

    // PUT /refunds/{id}/reject
    @PutMapping("/{id}/reject")
    public ResponseEntity<RefundResponseDTO> rejectRefund(@PathVariable Integer id) {
        return ResponseEntity.ok(refundService.rejectRefund(id));
    }
}