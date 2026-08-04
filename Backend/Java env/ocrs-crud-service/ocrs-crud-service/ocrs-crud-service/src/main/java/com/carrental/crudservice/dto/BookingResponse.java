package com.carrental.crudservice.dto;

import com.carrental.crudservice.entity.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponse {
    private Integer bookingId;
    private Integer customerId;
    private Integer vehicleId;
    private String vehicleRegistrationNumber;
    private String modelName;
    private LocalDateTime bookingDate;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private String dropCity;
    private BookingStatus status;
    private BigDecimal totalAmount;

    public BookingResponse() {
    }

    public BookingResponse(Integer bookingId, Integer customerId, Integer vehicleId, String vehicleRegistrationNumber, String modelName, LocalDateTime bookingDate, LocalDate pickupDate, LocalDate returnDate, String dropCity, BookingStatus status, BigDecimal totalAmount) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        this.modelName = modelName;
        this.bookingDate = bookingDate;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.dropCity = dropCity;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
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

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public static BookingResponseBuilder builder() {
        return new BookingResponseBuilder();
    }

    public static class BookingResponseBuilder {
        private Integer bookingId;
        private Integer customerId;
        private Integer vehicleId;
        private String vehicleRegistrationNumber;
        private String modelName;
        private LocalDateTime bookingDate;
        private LocalDate pickupDate;
        private LocalDate returnDate;
        private String dropCity;
        private BookingStatus status;
        private BigDecimal totalAmount;

        public BookingResponseBuilder bookingId(Integer bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public BookingResponseBuilder customerId(Integer customerId) {
            this.customerId = customerId;
            return this;
        }

        public BookingResponseBuilder vehicleId(Integer vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public BookingResponseBuilder vehicleRegistrationNumber(String vehicleRegistrationNumber) {
            this.vehicleRegistrationNumber = vehicleRegistrationNumber;
            return this;
        }

        public BookingResponseBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public BookingResponseBuilder bookingDate(LocalDateTime bookingDate) {
            this.bookingDate = bookingDate;
            return this;
        }

        public BookingResponseBuilder pickupDate(LocalDate pickupDate) {
            this.pickupDate = pickupDate;
            return this;
        }

        public BookingResponseBuilder returnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public BookingResponseBuilder dropCity(String dropCity) {
            this.dropCity = dropCity;
            return this;
        }

        public BookingResponseBuilder status(BookingStatus status) {
            this.status = status;
            return this;
        }

        public BookingResponseBuilder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public BookingResponse build() {
            return new BookingResponse(bookingId, customerId, vehicleId, vehicleRegistrationNumber, modelName, bookingDate, pickupDate, returnDate, dropCity, status, totalAmount);
        }
    }
}
