package com.carrental.bookingservice.repository;

import com.carrental.bookingservice.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Integer> {

    Optional<Refund> findByPaymentId(Integer paymentId);

    List<Refund> findByPaymentIdIn(List<Integer> paymentIds);
}
