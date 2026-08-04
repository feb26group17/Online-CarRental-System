package com.carrental.bookingservice.dto;

import com.carrental.bookingservice.entity.enums.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponse {

    private Integer refundId;
    private Integer paymentId;
    private BigDecimal refAmount;
    private String reason;
    private RefundStatus status;
    private LocalDateTime refundDate;
    private PaymentResponse paymentDetails;
}
