package com.carrental.crudservice.dto;

import jakarta.validation.constraints.NotNull;

public class RefundRequest {

    @NotNull(message = "paymentId is required")
    private Integer paymentId;

    private String reason;

    public RefundRequest() {
    }

    public RefundRequest(Integer paymentId, String reason) {
        this.paymentId = paymentId;
        this.reason = reason;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static RefundRequestBuilder builder() {
        return new RefundRequestBuilder();
    }

    public static class RefundRequestBuilder {
        private Integer paymentId;
        private String reason;

        public RefundRequestBuilder paymentId(Integer paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public RefundRequestBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public RefundRequest build() {
            return new RefundRequest(paymentId, reason);
        }
    }
}
