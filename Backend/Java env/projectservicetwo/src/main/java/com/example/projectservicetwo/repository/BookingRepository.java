package com.example.projectservicetwo.repository;

import com.example.projectservicetwo.entity.Booking;
import com.example.projectservicetwo.entity.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByCustomerCustomerId(Integer customerId);

    List<Booking> findByVehicleVehicleId(Integer vehicleId);
}