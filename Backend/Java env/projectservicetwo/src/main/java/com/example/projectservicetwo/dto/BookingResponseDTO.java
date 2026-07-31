package com.example.projectservicetwo.dto;

import com.example.projectservicetwo.entity.Booking.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponseDTO {

    private Integer bookingId;
    private CustomerResponseDTO customer;
    private VehicleResponseDTO vehicle;
    private LocalDateTime bookingDate;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private BookingStatus status;

    public BookingResponseDTO() {
    }

    public BookingResponseDTO(Integer bookingId, CustomerResponseDTO customer, VehicleResponseDTO vehicle,
                              LocalDateTime bookingDate, LocalDate pickupDate, LocalDate returnDate,
                              BookingStatus status) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.bookingDate = bookingDate;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters and Setters
    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public CustomerResponseDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerResponseDTO customer) {
        this.customer = customer;
    }

    public VehicleResponseDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleResponseDTO vehicle) {
        this.vehicle = vehicle;
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

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}