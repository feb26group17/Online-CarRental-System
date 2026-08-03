package com.carrental.crudservice.dto;

import com.carrental.crudservice.entity.enums.RefundStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RefundResponse {
    private Integer refundId;
    private Integer paymentId;
    private BigDecimal refAmount;
    private String reason;
    private RefundStatus status;
    private LocalDateTime refundDate;

    public RefundResponse() {
    }

    public RefundResponse(Integer refundId, Integer paymentId, BigDecimal refAmount, String reason, RefundStatus status, LocalDateTime refundDate) {
        this.refundId = refundId;
        this.paymentId = paymentId;
        this.refAmount = refAmount;
        this.reason = reason;
        this.status = status;
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

    public RefundStatus getStatus() {
        return status;
    }

    public void setStatus(RefundStatus status) {
        this.status = status;
    }

    public LocalDateTime getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(LocalDateTime refundDate) {
        this.refundDate = refundDate;
    }

    public static RefundResponseBuilder builder() {
        return new RefundResponseBuilder();
    }

    public static class RefundResponseBuilder {
        private Integer refundId;
        private Integer paymentId;
        private BigDecimal refAmount;
        private String reason;
        private RefundStatus status;
        private LocalDateTime refundDate;

        public RefundResponseBuilder refundId(Integer refundId) {
            this.refundId = refundId;
            return this;
        }

        public RefundResponseBuilder paymentId(Integer paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public RefundResponseBuilder refAmount(BigDecimal refAmount) {
            this.refAmount = refAmount;
            return this;
        }

        public RefundResponseBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public RefundResponseBuilder status(RefundStatus status) {
            this.status = status;
            return this;
        }

        public RefundResponseBuilder refundDate(LocalDateTime refundDate) {
            this.refundDate = refundDate;
            return this;
        }

        public RefundResponse build() {
            return new RefundResponse(refundId, paymentId, refAmount, reason, status, refundDate);
        }
    }
}
