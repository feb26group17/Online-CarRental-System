package com.carrental.bookingservice.dto;

import com.carrental.bookingservice.entity.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "Booking ID is required")
    private Integer bookingId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
