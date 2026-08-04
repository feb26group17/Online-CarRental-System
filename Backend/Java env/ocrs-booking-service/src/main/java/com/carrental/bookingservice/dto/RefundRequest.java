package com.carrental.bookingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefundRequest {

    @NotNull(message = "Payment ID is required")
    private Integer paymentId;

    @NotBlank(message = "Reason for refund is required")
    private String reason;
}
