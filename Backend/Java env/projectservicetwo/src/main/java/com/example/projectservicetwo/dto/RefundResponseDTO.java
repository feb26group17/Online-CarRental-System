package com.example.projectservicetwo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RefundResponseDTO {

    private Integer refundId;
    private PaymentResponseDTO payment;
    private BigDecimal refundAmount;
    private String reason;
    private LocalDateTime refundDate;

    public RefundResponseDTO() {
    }

    public RefundResponseDTO(Integer refundId, PaymentResponseDTO payment, BigDecimal refundAmount,
                             String reason, LocalDateTime refundDate) {
        this.refundId = refundId;
        this.payment = payment;
        this.refundAmount = refundAmount;
        this.reason = reason;
        this.refundDate = refundDate;
    }

    // Getters and Setters
    public Integer getRefundId() {
        return refundId;
    }

    public void setRefundId(Integer refundId) {
        this.refundId = refundId;
    }

    public PaymentResponseDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentResponseDTO payment) {
        this.payment = payment;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(LocalDateTime refundDate) {
        this.refundDate = refundDate;
    }
}