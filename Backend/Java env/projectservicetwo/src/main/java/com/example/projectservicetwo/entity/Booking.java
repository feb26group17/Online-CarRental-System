package com.example.projectservicetwo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {
	
	public enum BookingStatus {

	    Pending,
	    Confirmed,
	    Cancelled,
	    Completed
	}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;


    @Column(name = "booking_date", updatable = false)
    private LocalDateTime bookingDate;


    @Column(name = "pickup_date", nullable = false)
    private LocalDate pickupDate;


    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;


    // Default Constructor
    public Booking() {
    }


    // Parameterized Constructor
    public Booking(Customer customer, Vehicle vehicle,
                   LocalDate pickupDate, LocalDate returnDate) {

        this.customer = customer;
        this.vehicle = vehicle;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.bookingDate = LocalDateTime.now();
        this.status = BookingStatus.Pending;
    }


    // Automatically set booking date before saving
    @PrePersist
    public void prePersist() {
        this.bookingDate = LocalDateTime.now();

        if(this.status == null) {
            this.status = BookingStatus.Pending;
        }
    }


    // Getters and Setters

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
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