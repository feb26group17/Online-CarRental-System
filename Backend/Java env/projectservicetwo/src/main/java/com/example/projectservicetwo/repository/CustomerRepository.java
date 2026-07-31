package com.example.projectservicetwo.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projectservicetwo.entity.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {



}
