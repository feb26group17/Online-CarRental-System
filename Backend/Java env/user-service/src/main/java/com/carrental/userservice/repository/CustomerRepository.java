package com.carrental.userservice.repository;

import com.carrental.userservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByUserId(Integer userId);
    boolean existsByEmail(String email);
}
