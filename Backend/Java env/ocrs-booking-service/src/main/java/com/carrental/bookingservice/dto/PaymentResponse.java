package com.carrental.bookingservice.dto;

import com.carrental.bookingservice.entity.enums.PaymentMethod;
import com.carrental.bookingservice.entity.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Integer paymentId;
    private Integer bookingId;
    private BigDecimal amt;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private BookingResponse bookingDetails;
}
