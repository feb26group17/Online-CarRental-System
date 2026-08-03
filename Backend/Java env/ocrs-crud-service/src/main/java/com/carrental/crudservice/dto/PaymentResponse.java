package com.carrental.crudservice.dto;

import com.carrental.crudservice.entity.enums.PaymentMethod;
import com.carrental.crudservice.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {
    private Integer paymentId;
    private Integer bookingId;
    private BigDecimal amt;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;

    public PaymentResponse() {
    }

    public PaymentResponse(Integer paymentId, Integer bookingId, BigDecimal amt, PaymentMethod paymentMethod, PaymentStatus paymentStatus, LocalDateTime paymentDate) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.amt = amt;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
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

    public static PaymentResponseBuilder builder() {
        return new PaymentResponseBuilder();
    }

    public static class PaymentResponseBuilder {
        private Integer paymentId;
        private Integer bookingId;
        private BigDecimal amt;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;
        private LocalDateTime paymentDate;

        public PaymentResponseBuilder paymentId(Integer paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public PaymentResponseBuilder bookingId(Integer bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public PaymentResponseBuilder amt(BigDecimal amt) {
            this.amt = amt;
            return this;
        }

        public PaymentResponseBuilder paymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public PaymentResponseBuilder paymentStatus(PaymentStatus paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public PaymentResponseBuilder paymentDate(LocalDateTime paymentDate) {
            this.paymentDate = paymentDate;
            return this;
        }

        public PaymentResponse build() {
            return new PaymentResponse(paymentId, bookingId, amt, paymentMethod, paymentStatus, paymentDate);
        }
    }
}
