package com.example.projectservicetwo.service;

import com.example.projectservicetwo.dto.*;
import com.example.projectservicetwo.entity.*;
import com.example.projectservicetwo.entity.Payment.PaymentStatus;
import com.example.projectservicetwo.repository.PaymentRepository;
import com.example.projectservicetwo.repository.RefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public RefundService(RefundRepository refundRepository, PaymentRepository paymentRepository) {
        this.refundRepository = refundRepository;
        this.paymentRepository = paymentRepository;
    }

    // Get all refunds
    @Transactional(readOnly = true)
    public List<RefundResponseDTO> getAllRefunds() {
        return refundRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get refund by ID
    @Transactional(readOnly = true)
    public RefundResponseDTO getRefundById(Integer id) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refund not found with id: " + id));
        return mapToDTO(refund);
    }

    // Approve refund (updates associated Payment status to Refunded)
    @Transactional
    public RefundResponseDTO approveRefund(Integer id) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refund not found with id: " + id));

        Payment payment = refund.getPayment();
        if (payment != null) {
            payment.setPaymentStatus(PaymentStatus.Refunded);
            paymentRepository.save(payment);
        }

        return mapToDTO(refund);
    }

    // Reject refund request
    @Transactional
    public RefundResponseDTO rejectRefund(Integer id) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refund not found with id: " + id));

        // Keep payment status as Paid / unchanged, or perform custom rejection logic
        return mapToDTO(refund);
    }

    // Helper method to convert Refund Entity to RefundResponseDTO
    private RefundResponseDTO mapToDTO(Refund refund) {
        PaymentResponseDTO paymentDTO = null;

        if (refund.getPayment() != null) {
            Payment p = refund.getPayment();
            BookingResponseDTO bookingDTO = null;

            if (p.getBooking() != null) {
                Booking b = p.getBooking();

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

            paymentDTO = new PaymentResponseDTO(
                    p.getPaymentId(),
                    bookingDTO,
                    p.getAmount(),
                    p.getPaymentMethod(),
                    p.getPaymentStatus(),
                    p.getPaymentDate()
            );
        }

        return new RefundResponseDTO(
                refund.getRefundId(),
                paymentDTO,
                refund.getRefundAmount(),
                refund.getReason(),
                refund.getRefundDate()
        );
    }
}