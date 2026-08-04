package com.carrental.bookingservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotNull(message = "Vehicle ID is required")
    private Integer vehicleId;

    @NotNull(message = "Pickup date is required")
    @FutureOrPresent(message = "Pickup date cannot be in the past")
    private LocalDate pickupDate;

    @NotNull(message = "Return date is required")
    @Future(message = "Return date must be in the future")
    private LocalDate returnDate;

    private String dropCity;
}
