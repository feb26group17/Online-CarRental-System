package com.carrental.bookingservice.entity;

import com.carrental.bookingservice.entity.enums.RefundStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "refund")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_id")
    private Integer refundId;

    @Column(name = "payment_id", nullable = false)
    private Integer paymentId;

    @Column(name = "ref_amount", precision = 10, scale = 2)
    private BigDecimal refAmount;

    @Column(name = "refund_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "refund_reason", columnDefinition = "TEXT")
    private String refundReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private RefundStatus status = RefundStatus.Approved;

    @Enumerated(EnumType.STRING)
    @Column(name = "refund_status")
    @Builder.Default
    private RefundStatus refundStatus = RefundStatus.Approved;

    @Column(name = "refund_date", insertable = false, updatable = false)
    private LocalDateTime refundDate;
}
