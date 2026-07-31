package com.example.projectservicetwo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "refund")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_id")
    private Integer refundId;


    // Many refunds can belong to one payment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;


    @Column(name = "ref_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount;


    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;


    @Column(name = "refund_date", updatable = false)
    private LocalDateTime refundDate;


    // Default Constructor
    public Refund() {
    }


    // Parameterized Constructor
    public Refund(Payment payment, BigDecimal refundAmount, String reason) {
        this.payment = payment;
        this.refundAmount = refundAmount;
        this.reason = reason;
        this.refundDate = LocalDateTime.now();
    }


    // Automatically set refund date before saving
    @PrePersist
    public void prePersist() {
        this.refundDate = LocalDateTime.now();
    }


    // Getters and Setters

    public Integer getRefundId() {
        return refundId;
    }

    public void setRefundId(Integer refundId) {
        this.refundId = refundId;
    }


    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
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