

package com.carrental.crudservice.entity;
 
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
 
    @Column(name = "payment_id", nullable = false)
    private Integer paymentId;
 
    @Column(name = "ref_amount", precision = 10, scale = 2)
    private BigDecimal refAmount;
 
    @Column(columnDefinition = "TEXT")
    private String reason;
 
    @Column(name = "refund_date", insertable = false, updatable = false)
    private LocalDateTime refundDate;
 
    public Refund() {
    }
 
    public Refund(Integer refundId, Integer paymentId, BigDecimal refAmount, String reason, LocalDateTime refundDate) {
        this.refundId = refundId;
        this.paymentId = paymentId;
        this.refAmount = refAmount;
        this.reason = reason;
        this.refundDate = refundDate;
    }
 
    public Integer getRefundId() {
        return refundId;
    }
 
    public void setRefundId(Integer refundId) {
        this.refundId = refundId;
    }
 
    public Integer getPaymentId() {
        return paymentId;
    }
 
    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }
 
    public BigDecimal getRefAmount() {
        return refAmount;
    }
 
    public void setRefAmount(BigDecimal refAmount) {
        this.refAmount = refAmount;
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
 
    public static RefundBuilder builder() {
        return new RefundBuilder();
    }
 
    public static class RefundBuilder {
        private Integer refundId;
        private Integer paymentId;
        private BigDecimal refAmount;
        private String reason;
        private LocalDateTime refundDate;
 
        public RefundBuilder refundId(Integer refundId) {
            this.refundId = refundId;
            return this;
        }
 
        public RefundBuilder paymentId(Integer paymentId) {
            this.paymentId = paymentId;
            return this;
        }
 
        public RefundBuilder refAmount(BigDecimal refAmount) {
            this.refAmount = refAmount;
            return this;
        }
 
        public RefundBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }
 
        public RefundBuilder refundDate(LocalDateTime refundDate) {
            this.refundDate = refundDate;
            return this;
        }
 
        public Refund build() {
            return new Refund(refundId, paymentId, refAmount, reason, refundDate);
        }
    }
}
 
