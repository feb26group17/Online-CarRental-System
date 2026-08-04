package com.carrental.bookingservice.service;

import com.carrental.bookingservice.client.CrudServiceClient;
import com.carrental.bookingservice.dto.BookingResponse;
import com.carrental.bookingservice.dto.PaymentRequest;
import com.carrental.bookingservice.dto.PaymentResponse;
import com.carrental.bookingservice.entity.Booking;
import com.carrental.bookingservice.entity.Customer;
import com.carrental.bookingservice.entity.Payment;
import com.carrental.bookingservice.entity.Vehicle;
import com.carrental.bookingservice.entity.enums.BookingStatus;
import com.carrental.bookingservice.entity.enums.PaymentStatus;
import com.carrental.bookingservice.repository.BookingRepository;
import com.carrental.bookingservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final CrudServiceClient crudServiceClient;
    private final BookingService bookingService;

    public PaymentService(PaymentRepository paymentRepository,
                          BookingRepository bookingRepository,
                          CrudServiceClient crudServiceClient,
                          BookingService bookingService) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.crudServiceClient = crudServiceClient;
        this.bookingService = bookingService;
    }

    @Transactional
    public PaymentResponse createPayment(Integer userId, PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + request.getBookingId()));

        Customer customer = crudServiceClient.getCustomerByUserId(userId);

        if (!booking.getCustomerId().equals(customer.getCustomerId())) {
            throw new RuntimeException("Unauthorized: You can only make payments for your own bookings");
        }

        if (paymentRepository.findByBookingId(booking.getBookingId()).isPresent()) {
            throw new RuntimeException("Payment has already been processed for this booking");
        }

        if (booking.getStatus() == BookingStatus.Cancelled || booking.getStatus() == BookingStatus.Completed) {
            throw new RuntimeException("Cannot process payment for booking in status: " + booking.getStatus());
        }

        Vehicle vehicle = crudServiceClient.getVehicleById(booking.getVehicleId());

        long days = ChronoUnit.DAYS.between(booking.getPickupDate(), booking.getReturnDate());
        if (days <= 0) days = 1;
        BigDecimal amount = vehicle.getRentPerDay().multiply(BigDecimal.valueOf(days));

        // Populate both amt and amount fields for database schema compatibility
        Payment payment = Payment.builder()
                .bookingId(booking.getBookingId())
                .amt(amount)
                .amount(amount)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.Paid)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Update booking status to Confirmed
        booking.setStatus(BookingStatus.Confirmed);
        bookingRepository.save(booking);

        // Update vehicle status to Booked via RestTemplate
        crudServiceClient.updateVehicleStatus(vehicle.getVehicleId(), "Booked");

        BookingResponse bookingDetails = bookingService.getBookingById(booking.getBookingId(), userId, "CUSTOMER");

        return mapToResponse(savedPayment, bookingDetails);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream().map(p -> {
            BookingResponse bookingDetails = bookingService.getBookingById(p.getBookingId(), null, "ADMIN");
            return mapToResponse(p, bookingDetails);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getMyPaymentHistory(Integer userId) {
        Customer customer = crudServiceClient.getCustomerByUserId(userId);

        List<Booking> bookings = bookingRepository.findByCustomerId(customer.getCustomerId());
        List<Integer> bookingIds = bookings.stream().map(Booking::getBookingId).collect(Collectors.toList());

        List<Payment> payments = paymentRepository.findByBookingIdIn(bookingIds);
        return payments.stream().map(p -> {
            BookingResponse bookingDetails = bookingService.getBookingById(p.getBookingId(), userId, "CUSTOMER");
            return mapToResponse(p, bookingDetails);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByBookingId(Integer bookingId, Integer userId, String role) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking ID: " + bookingId));

        BookingResponse bookingDetails = bookingService.getBookingById(bookingId, userId, role);
        return mapToResponse(payment, bookingDetails);
    }

    private PaymentResponse mapToResponse(Payment payment, BookingResponse bookingDetails) {
        BigDecimal paidAmount = payment.getAmt() != null ? payment.getAmt() : payment.getAmount();

        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .bookingId(payment.getBookingId())
                .amt(paidAmount)
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .paymentDate(payment.getPaymentDate())
                .bookingDetails(bookingDetails)
                .build();
    }
}
