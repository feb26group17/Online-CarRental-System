package com.carrental.bookingservice.dto;

import com.carrental.bookingservice.entity.enums.BookingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Integer bookingId;
    private Integer customerId;
    private Integer vehicleId;
    private String vehicleRegistrationNumber;
    private String brandName;
    private String modelName;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private LocalDateTime bookingDate;
    private String dropCity;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private BigDecimal rentPerDay;
}
