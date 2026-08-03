package com.carrental.crudservice.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class BookingRequest {

    @NotNull(message = "vehicleId is required")
    private Integer vehicleId;

    @NotNull(message = "pickupDate is required")
    private LocalDate pickupDate;

    @NotNull(message = "returnDate is required")
    private LocalDate returnDate;

    private String dropCity;

    public BookingRequest() {
    }

    public BookingRequest(Integer vehicleId, LocalDate pickupDate, LocalDate returnDate, String dropCity) {
        this.vehicleId = vehicleId;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.dropCity = dropCity;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getDropCity() {
        return dropCity;
    }

    public void setDropCity(String dropCity) {
        this.dropCity = dropCity;
    }

    public static BookingRequestBuilder builder() {
        return new BookingRequestBuilder();
    }

    public static class BookingRequestBuilder {
        private Integer vehicleId;
        private LocalDate pickupDate;
        private LocalDate returnDate;
        private String dropCity;

        public BookingRequestBuilder vehicleId(Integer vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public BookingRequestBuilder pickupDate(LocalDate pickupDate) {
            this.pickupDate = pickupDate;
            return this;
        }

        public BookingRequestBuilder returnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public BookingRequestBuilder dropCity(String dropCity) {
            this.dropCity = dropCity;
            return this;
        }

        public BookingRequest build() {
            return new BookingRequest(vehicleId, pickupDate, returnDate, dropCity);
        }
    }
}
