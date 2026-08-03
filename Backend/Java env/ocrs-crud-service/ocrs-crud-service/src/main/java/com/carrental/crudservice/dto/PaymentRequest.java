package com.carrental.crudservice.dto;

import com.carrental.crudservice.entity.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public class PaymentRequest {

    @NotNull(message = "bookingId is required")
    private Integer bookingId;

    @NotNull(message = "paymentMethod is required")
    private PaymentMethod paymentMethod;

    public PaymentRequest() {
    }

    public PaymentRequest(Integer bookingId, PaymentMethod paymentMethod) {
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public static PaymentRequestBuilder builder() {
        return new PaymentRequestBuilder();
    }

    public static class PaymentRequestBuilder {
        private Integer bookingId;
        private PaymentMethod paymentMethod;

        public PaymentRequestBuilder bookingId(Integer bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public PaymentRequestBuilder paymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public PaymentRequest build() {
            return new PaymentRequest(bookingId, paymentMethod);
        }
    }
}
