package com.carrental.userservice.repository;

import com.carrental.userservice.entity.CarOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarOwnerRepository extends JpaRepository<CarOwner, Integer> {
    Optional<CarOwner> findByEmail(String email);
    Optional<CarOwner> findByUserId(Integer userId);
    boolean existsByEmail(String email);
}
