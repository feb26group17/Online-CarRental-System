package com.example.projectservicetwo.service;

import com.example.projectservicetwo.dto.*;
import com.example.projectservicetwo.entity.*;
import com.example.projectservicetwo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Get all payments
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get payment by ID
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentById(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return mapToDTO(payment);
    }

    // Helper method to convert Payment Entity to PaymentResponseDTO
    private PaymentResponseDTO mapToDTO(Payment payment) {
        BookingResponseDTO bookingDTO = null;

        if (payment.getBooking() != null) {
            Booking b = payment.getBooking();

            // Map Customer
            CustomerResponseDTO customerDTO = null;
            if (b.getCustomer() != null) {
                Customer c = b.getCustomer();
                UserResponseDTO custUserDTO = null;
                if (c.getUser() != null) {
                    User u = c.getUser();
                    custUserDTO = new UserResponseDTO(
                            u.getId(), u.getName(), u.getEmail(), u.getPhone(),
                            u.getRole(), u.getAddress(), u.getStatus(),
                            u.getAdharCard(), u.getCreatedAt()
                    );
                }
                customerDTO = new CustomerResponseDTO(
                        c.getCustomerId(),
                        c.getDrivingLicense(),
                        custUserDTO
                );
            }

            // Map Vehicle
            VehicleResponseDTO vehicleDTO = null;
            if (b.getVehicle() != null) {
                Vehicle v = b.getVehicle();
                UserResponseDTO ownerDTO = null;
                if (v.getOwner() != null) {
                    User o = v.getOwner();
                    ownerDTO = new UserResponseDTO(
                            o.getId(), o.getName(), o.getEmail(), o.getPhone(),
                            o.getRole(), o.getAddress(), o.getStatus(),
                            o.getAdharCard(), o.getCreatedAt()
                    );
                }
                Integer modelId = (v.getModel() != null) ? v.getModel().getModelId() : null;

                vehicleDTO = new VehicleResponseDTO(
                        v.getVehicleId(), ownerDTO, modelId,
                        v.getRegistrationNumber(), v.getFuelType(),
                        v.getRentPerDay(), v.getStatus()
                );
            }

            bookingDTO = new BookingResponseDTO(
                    b.getBookingId(), customerDTO, vehicleDTO,
                    b.getBookingDate(), b.getPickupDate(),
                    b.getReturnDate(), b.getStatus()
            );
        }

        return new PaymentResponseDTO(
                payment.getPaymentId(),
                bookingDTO,
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getPaymentDate()
        );
    }
}