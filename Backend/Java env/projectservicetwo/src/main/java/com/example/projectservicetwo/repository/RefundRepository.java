package com.example.projectservicetwo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projectservicetwo.entity.Refund;


@Repository
public interface RefundRepository extends JpaRepository<Refund, Integer> {



}
