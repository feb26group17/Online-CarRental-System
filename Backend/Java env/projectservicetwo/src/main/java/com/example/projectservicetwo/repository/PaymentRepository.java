package com.example.projectservicetwo.repository;

import com.example.projectservicetwo.entity.Payment;
import com.example.projectservicetwo.entity.Payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    List<Payment> findByBookingBookingId(Integer bookingId);
}