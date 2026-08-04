package com.carrental.bookingservice.repository;

import com.carrental.bookingservice.entity.Booking;
import com.carrental.bookingservice.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByCustomerId(Integer customerId);

    List<Booking> findByVehicleId(Integer vehicleId);

    boolean existsByVehicleIdAndStatusNotAndPickupDateLessThanEqualAndReturnDateGreaterThanEqual(
            Integer vehicleId,
            BookingStatus status,
            LocalDate returnDate,
            LocalDate pickupDate
    );
}
