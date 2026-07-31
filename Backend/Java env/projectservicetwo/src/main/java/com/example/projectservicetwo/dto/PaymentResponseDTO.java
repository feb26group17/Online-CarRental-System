package com.example.projectservicetwo.dto;

import com.example.projectservicetwo.entity.Payment.PaymentMethod;
import com.example.projectservicetwo.entity.Payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponseDTO {

    private Integer paymentId;
    private BookingResponseDTO booking;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;

    public PaymentResponseDTO() {
    }

    public PaymentResponseDTO(Integer paymentId, BookingResponseDTO booking, BigDecimal amount,
                              PaymentMethod paymentMethod, PaymentStatus paymentStatus,
                              LocalDateTime paymentDate) {
        this.paymentId = paymentId;
        this.booking = booking;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    // Getters and Setters
    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public BookingResponseDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingResponseDTO booking) {
        this.booking = booking;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}