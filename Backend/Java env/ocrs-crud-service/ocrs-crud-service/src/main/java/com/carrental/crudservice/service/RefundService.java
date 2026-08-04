package com.carrental.crudservice.service;

import com.carrental.crudservice.dto.RefundRequest;
import com.carrental.crudservice.dto.RefundResponse;
import com.carrental.crudservice.entity.Payment;
import com.carrental.crudservice.entity.Refund;
import com.carrental.crudservice.entity.enums.PaymentStatus;
import com.carrental.crudservice.entity.enums.RefundStatus;
import com.carrental.crudservice.repository.PaymentRepository;
import com.carrental.crudservice.repository.RefundRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;

    public RefundService(RefundRepository refundRepository, PaymentRepository paymentRepository) {
        this.refundRepository = refundRepository;
        this.paymentRepository = paymentRepository;
    }

    public RefundResponse requestRefund(RefundRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + request.getPaymentId()));

        Refund refund = Refund.builder()
                .paymentId(payment.getPaymentId())
                .refAmount(payment.getAmt())
                .reason(request.getReason())
                .status(RefundStatus.Pending)
                .build();

        Refund saved = refundRepository.save(refund);
        return mapToResponse(saved);
    }

    public RefundResponse getRefundByPaymentId(Integer paymentId) {
        Refund refund = refundRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Refund not found for payment id: " + paymentId));
        return mapToResponse(refund);
    }

    public List<RefundResponse> getAllRefunds() {
        return refundRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public RefundResponse updateRefundStatus(Integer id, String statusStr) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refund not found with id: " + id));

        RefundStatus status = RefundStatus.valueOf(statusStr);
        refund.setStatus(status);
        Refund updated = refundRepository.save(refund);

        if (status == RefundStatus.Approved || status == RefundStatus.Completed) {
            paymentRepository.findById(refund.getPaymentId()).ifPresent(p -> {
                p.setPaymentStatus(PaymentStatus.Refunded);
                paymentRepository.save(p);
            });
        }

        return mapToResponse(updated);
    }

    private RefundResponse mapToResponse(Refund refund) {
        return RefundResponse.builder()
                .refundId(refund.getRefundId())
                .paymentId(refund.getPaymentId())
                .refAmount(refund.getRefAmount())
                .reason(refund.getReason())
                .status(refund.getStatus())
                .refundDate(refund.getRefundDate())
                .build();
    }
}
