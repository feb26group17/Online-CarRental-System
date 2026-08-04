package com.carrental.bookingservice.repository;

import com.carrental.bookingservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByBookingId(Integer bookingId);

    List<Payment> findByBookingIdIn(List<Integer> bookingIds);
}
