package com.example.projectservicetwo.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projectservicetwo.entity.Booking;
import com.example.projectservicetwo.entity.Booking.BookingStatus;
import com.example.projectservicetwo.entity.Customer;
import com.example.projectservicetwo.entity.Vehicle;



@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {


  
}
