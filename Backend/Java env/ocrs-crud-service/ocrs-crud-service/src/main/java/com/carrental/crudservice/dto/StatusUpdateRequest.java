package com.carrental.crudservice.dto;

public class StatusUpdateRequest {
    private String status;

    public StatusUpdateRequest() {
    }

    public StatusUpdateRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static StatusUpdateRequestBuilder builder() {
        return new StatusUpdateRequestBuilder();
    }

    public static class StatusUpdateRequestBuilder {
        private String status;

        public StatusUpdateRequestBuilder status(String status) {
            this.status = status;
            return this;
        }

        public StatusUpdateRequest build() {
            return new StatusUpdateRequest(status);
        }
    }
}
