package com.carrental.crudservice.repository;

import com.carrental.crudservice.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Integer> {
    Optional<Refund> findByPaymentId(Integer paymentId);
}
