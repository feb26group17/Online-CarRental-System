package com.carrental.crudservice.service;

import com.carrental.crudservice.dto.PaymentRequest;
import com.carrental.crudservice.dto.PaymentResponse;
import com.carrental.crudservice.entity.Booking;
import com.carrental.crudservice.entity.Customer;
import com.carrental.crudservice.entity.Payment;
import com.carrental.crudservice.entity.Vehicle;
import com.carrental.crudservice.entity.enums.BookingStatus;
import com.carrental.crudservice.entity.enums.PaymentStatus;
import com.carrental.crudservice.repository.BookingRepository;
import com.carrental.crudservice.repository.CustomerRepository;
import com.carrental.crudservice.repository.PaymentRepository;
import com.carrental.crudservice.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          BookingRepository bookingRepository,
                          CustomerRepository customerRepository,
                          VehicleRepository vehicleRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public PaymentResponse createPayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + request.getBookingId()));

        Vehicle vehicle = vehicleRepository.findById(booking.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + booking.getVehicleId()));

        long days = ChronoUnit.DAYS.between(booking.getPickupDate(), booking.getReturnDate());
        if (days <= 0) days = 1;
        BigDecimal amount = vehicle.getRentPerDay().multiply(BigDecimal.valueOf(days));

        Payment payment = Payment.builder()
                .bookingId(booking.getBookingId())
                .amt(amount)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.Paid)
                .build();

        Payment saved = paymentRepository.save(payment);

        // Update booking status to Confirmed
        booking.setStatus(BookingStatus.Confirmed);
        bookingRepository.save(booking);

        return mapToResponse(saved);
    }

    public List<PaymentResponse> getMyPaymentHistory(Integer userId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer profile not found for user: " + userId));

        List<Booking> bookings = bookingRepository.findByCustomerId(customer.getCustomerId());
        List<Integer> bookingIds = bookings.stream().map(Booking::getBookingId).collect(Collectors.toList());

        List<Payment> payments = paymentRepository.findByBookingIdIn(bookingIds);
        return payments.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public PaymentResponse getPaymentByBookingId(Integer bookingId, Integer userId, String role) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking id: " + bookingId));

        if (!"ADMIN".equalsIgnoreCase(role)) {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

            if ("CUSTOMER".equalsIgnoreCase(role)) {
                Customer customer = customerRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Customer profile not found for user: " + userId));
                if (!booking.getCustomerId().equals(customer.getCustomerId())) {
                    throw new RuntimeException("Unauthorized: you do not have access to this payment");
                }
            } else if ("OWNER".equalsIgnoreCase(role)) {
                Vehicle vehicle = vehicleRepository.findById(booking.getVehicleId())
                        .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + booking.getVehicleId()));
                if (!vehicle.getUserId().equals(userId)) {
                    throw new RuntimeException("Unauthorized: you do not have access to this payment");
                }
            } else {
                throw new RuntimeException("Unauthorized: you do not have access to this payment");
            }
        }

        return mapToResponse(payment);
    }

    public List<PaymentResponse> getPaymentsForVehicle(Integer vehicleId, Integer userId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));

        if (!vehicle.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized: you do not own this vehicle");
        }

        List<Booking> bookings = bookingRepository.findByVehicleId(vehicleId);
        List<Integer> bookingIds = bookings.stream().map(Booking::getBookingId).collect(Collectors.toList());

        List<Payment> payments = paymentRepository.findByBookingIdIn(bookingIds);
        return payments.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .bookingId(payment.getBookingId())
                .amt(payment.getAmt())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}
