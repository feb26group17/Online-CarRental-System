package com.example.projectservicetwo.repository;

import com.example.projectservicetwo.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Integer> {

    List<Refund> findByPaymentPaymentId(Integer paymentId);
}