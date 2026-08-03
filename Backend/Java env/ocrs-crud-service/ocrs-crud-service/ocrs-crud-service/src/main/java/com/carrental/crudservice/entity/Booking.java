package com.carrental.crudservice.entity;

import com.carrental.crudservice.entity.enums.BookingStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "vehicle_id", nullable = false)
    private Integer vehicleId;

    @Column(name = "booking_date", insertable = false, updatable = false)
    private LocalDateTime bookingDate;

    @Column(name = "pickup_date", nullable = false)
    private LocalDate pickupDate;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @Column(name = "drop_city", length = 50)
    private String dropCity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status = BookingStatus.Pending;

    public Booking() {
    }

    public Booking(Integer bookingId, Integer customerId, Integer vehicleId, LocalDateTime bookingDate, LocalDate pickupDate, LocalDate returnDate, String dropCity, BookingStatus status) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.bookingDate = bookingDate;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.dropCity = dropCity;
        this.status = status != null ? status : BookingStatus.Pending;
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

    public static BookingBuilder builder() {
        return new BookingBuilder();
    }

    public static class BookingBuilder {
        private Integer bookingId;
        private Integer customerId;
        private Integer vehicleId;
        private LocalDateTime bookingDate;
        private LocalDate pickupDate;
        private LocalDate returnDate;
        private String dropCity;
        private BookingStatus status = BookingStatus.Pending;

        public BookingBuilder bookingId(Integer bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public BookingBuilder customerId(Integer customerId) {
            this.customerId = customerId;
            return this;
        }

        public BookingBuilder vehicleId(Integer vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public BookingBuilder bookingDate(LocalDateTime bookingDate) {
            this.bookingDate = bookingDate;
            return this;
        }

        public BookingBuilder pickupDate(LocalDate pickupDate) {
            this.pickupDate = pickupDate;
            return this;
        }

        public BookingBuilder returnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public BookingBuilder dropCity(String dropCity) {
            this.dropCity = dropCity;
            return this;
        }

        public BookingBuilder status(BookingStatus status) {
            this.status = status;
            return this;
        }

        public Booking build() {
            return new Booking(bookingId, customerId, vehicleId, bookingDate, pickupDate, returnDate, dropCity, status);
        }
    }
}
