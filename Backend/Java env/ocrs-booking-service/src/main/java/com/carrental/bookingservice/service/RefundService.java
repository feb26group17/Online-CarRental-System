package com.carrental.bookingservice.service;

import com.carrental.bookingservice.client.CrudServiceClient;
import com.carrental.bookingservice.dto.RefundRequest;
import com.carrental.bookingservice.dto.RefundResponse;
import com.carrental.bookingservice.entity.Booking;
import com.carrental.bookingservice.entity.Customer;
import com.carrental.bookingservice.entity.Payment;
import com.carrental.bookingservice.entity.Refund;
import com.carrental.bookingservice.entity.enums.BookingStatus;
import com.carrental.bookingservice.entity.enums.PaymentStatus;
import com.carrental.bookingservice.entity.enums.RefundStatus;
import com.carrental.bookingservice.repository.BookingRepository;
import com.carrental.bookingservice.repository.PaymentRepository;
import com.carrental.bookingservice.repository.RefundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final CrudServiceClient crudServiceClient;

    public RefundService(RefundRepository refundRepository,
                         PaymentRepository paymentRepository,
                         BookingRepository bookingRepository,
                         CrudServiceClient crudServiceClient) {
        this.refundRepository = refundRepository;
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.crudServiceClient = crudServiceClient;
    }

    @Transactional
    public RefundResponse requestRefund(Integer userId, RefundRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + request.getPaymentId()));

        Booking booking = bookingRepository.findById(payment.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found for payment ID: " + request.getPaymentId()));

        Customer customer = crudServiceClient.getCustomerByUserId(userId);

        if (!booking.getCustomerId().equals(customer.getCustomerId())) {
            throw new RuntimeException("Unauthorized: You can only request refund for your own payments");
        }

        if (payment.getPaymentStatus() != PaymentStatus.Paid) {
            throw new RuntimeException("Refund can only be requested for Paid transactions");
        }

        if (refundRepository.findByPaymentId(payment.getPaymentId()).isPresent()) {
            throw new RuntimeException("Refund request has already been submitted for this payment");
        }

        BigDecimal refundAmt = payment.getAmt() != null ? payment.getAmt() : payment.getAmount();

        // Populate both column variations for DB schema compatibility
        Refund refund = Refund.builder()
                .paymentId(payment.getPaymentId())
                .refAmount(refundAmt)
                .refundAmount(refundAmt)
                .reason(request.getReason())
                .refundReason(request.getReason())
                .status(RefundStatus.Approved)
                .refundStatus(RefundStatus.Approved)
                .build();

        Refund savedRefund = refundRepository.save(refund);

        // Update payment status to Refunded
        payment.setPaymentStatus(PaymentStatus.Refunded);
        paymentRepository.save(payment);

        // Cancel associated booking
        booking.setStatus(BookingStatus.Cancelled);
        bookingRepository.save(booking);

        // Release vehicle via RestTemplate to crud-service
        crudServiceClient.updateVehicleStatus(booking.getVehicleId(), "Available");

        return mapToResponse(savedRefund);
    }

    @Transactional(readOnly = true)
    public List<RefundResponse> getAllRefunds() {
        return refundRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RefundResponse> getMyRefunds(Integer userId) {
        Customer customer = crudServiceClient.getCustomerByUserId(userId);

        List<Booking> bookings = bookingRepository.findByCustomerId(customer.getCustomerId());
        List<Integer> bookingIds = bookings.stream().map(Booking::getBookingId).collect(Collectors.toList());

        List<Payment> payments = paymentRepository.findByBookingIdIn(bookingIds);
        List<Integer> paymentIds = payments.stream().map(Payment::getPaymentId).collect(Collectors.toList());

        List<Refund> refunds = refundRepository.findByPaymentIdIn(paymentIds);
        return refunds.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public RefundResponse updateRefundStatus(Integer id, String statusStr) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refund not found with ID: " + id));

        RefundStatus status;
        try {
            status = RefundStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid refund status: " + statusStr);
        }

        refund.setStatus(status);
        refund.setRefundStatus(status);
        Refund updated = refundRepository.save(refund);
        return mapToResponse(updated);
    }

    private RefundResponse mapToResponse(Refund refund) {
        BigDecimal amt = refund.getRefAmount() != null ? refund.getRefAmount() : refund.getRefundAmount();
        String rReason = refund.getReason() != null ? refund.getReason() : refund.getRefundReason();
        RefundStatus rStatus = refund.getStatus() != null ? refund.getStatus() : refund.getRefundStatus();

        return RefundResponse.builder()
                .refundId(refund.getRefundId())
                .paymentId(refund.getPaymentId())
                .refAmount(amt)
                .reason(rReason)
                .status(rStatus)
                .refundDate(refund.getRefundDate())
                .build();
    }
}
